package fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.andr_dev_application.databinding.ButtonsNavBinding
import com.example.andr_dev_application.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import utils.HeroCardAdapter
import utils.HeroModel
import utils.StackLayoutManager
import utils.api.RetrofitInstance
import utils.FileUtils
import utils.api.HeroRepository


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val remainingHeroes = mutableSetOf<HeroModel>() // Use Set to prevent duplicates

    companion object {
        val likedHeroes = mutableListOf<HeroModel>()
        val dislikedHeroes = mutableListOf<HeroModel>()
    }

    private lateinit var heroCardAdapter: HeroCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupNavigation()
        observeHeroes()
        setupSwipeGesture()
        return binding.root
    }

    private fun setupNavigation() {
        val navBinding = ButtonsNavBinding.bind(binding.root)
        navBinding.buttonFunction1.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingFragments())
        }
        navBinding.buttonFunction3.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToRatedHeroesButtonsFragment())
        }
    }

    private fun observeHeroes() {
        val repository = RetrofitInstance.provideHeroRepository(requireContext())

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                repository.fetchHeroesFromLocal().collect { heroes ->
                    if (heroes.isEmpty()) {
                        fetchHeroes(repository)
                    } else {
                        remainingHeroes.clear()
                        remainingHeroes.addAll(heroes)
                        setupHeroRecyclerView(remainingHeroes.toList())
                    }
                }
            }
        }
    }


    private fun fetchHeroes(repository: HeroRepository) {
        lifecycleScope.launch {
            try {
                val localHeroes = repository.fetchHeroesFromLocal().first()

                if (localHeroes.isEmpty()) {
                    val apiHeroes = repository.fetchHeroes()
                    repository.saveHeroesToLocal(apiHeroes)
                    remainingHeroes.addAll(apiHeroes)

                    FileUtils.saveHeroesToFile(requireContext(), remainingHeroes.toList())
                        ?.let { file ->
                            Toast.makeText(requireContext(), "Heroes saved to ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                        } ?: Toast.makeText(requireContext(), "Failed to save heroes", Toast.LENGTH_SHORT).show()

                    setupHeroRecyclerView(remainingHeroes.toList())
                } else {
                    remainingHeroes.addAll(localHeroes)
                    setupHeroRecyclerView(remainingHeroes.toList())
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error fetching heroes: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupHeroRecyclerView(heroes: List<HeroModel>) {
        heroCardAdapter = HeroCardAdapter(heroes.toMutableList())
        heroCardAdapter.ensurePlaceholder() // Ensure placeholder is always present
        binding.recyclerView.adapter = heroCardAdapter
        binding.recyclerView.layoutManager = StackLayoutManager(requireContext())
    }

    private fun setupSwipeGesture() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val hero = heroCardAdapter.getHeroAt(position)

                    if (hero.isPlaceholder) {
                        heroCardAdapter.notifyItemChanged(position)
                        return
                    }

                    if (direction == ItemTouchHelper.RIGHT) {
                        likeHero(hero)
                    } else if (direction == ItemTouchHelper.LEFT) {
                        dislikeHero(hero)
                    }

                    lifecycleScope.launch {
                        val repository = RetrofitInstance.provideHeroRepository(requireContext())
                        repository.deleteHero(hero)
                    }

                    heroCardAdapter.removeHeroAt(position)
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun likeHero(hero: HeroModel) {
        likedHeroes.add(hero)
        Toast.makeText(requireContext(), "Liked ${hero.firstName}", Toast.LENGTH_SHORT).show()
    }

    private fun dislikeHero(hero: HeroModel) {
        dislikedHeroes.add(hero)
        Toast.makeText(requireContext(), "Disliked ${hero.firstName}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
