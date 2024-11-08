package fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.andr_dev_application.databinding.ButtonsNavBinding
import com.example.andr_dev_application.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import utils.HeroCardAdapter
import utils.HeroModel
import utils.StackLayoutManager
import utils.api.RetrofitInstance
import utils.FileUtils



class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        val likedHeroes = mutableListOf<HeroModel>()
        val dislikedHeroes = mutableListOf<HeroModel>()
        val remainingHeroes = mutableListOf<HeroModel>()
    }

    private lateinit var heroCardAdapter: HeroCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        setupNavigation()
        fetchHeroes()
        setupSwipeGesture()
        return view
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

    private fun fetchHeroes() {
        lifecycleScope.launch {
            try {
                if (remainingHeroes.isEmpty()) {
                    if (isAdded) {
                        val repository = RetrofitInstance.provideHeroRepository(requireContext())
                        val apiHeroes = repository.fetchHeroes()
                        repository.saveHeroesToLocal(apiHeroes)
                        val localHeroes = repository.fetchHeroesFromLocal()
                        remainingHeroes.addAll(localHeroes)

                        val file = FileUtils.saveHeroesToFile(requireContext(), localHeroes)
                        if (file != null) {
                            Toast.makeText(requireContext(), "Heroes saved to ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Failed to save heroes", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                if (isAdded) {
                    heroCardAdapter = HeroCardAdapter(remainingHeroes)
                    binding.recyclerView.adapter = heroCardAdapter
                    binding.recyclerView.layoutManager = StackLayoutManager(requireContext())
                    heroCardAdapter.ensurePlaceholder()
                }
            } catch (e: Exception) {
                if (isAdded) {
                    Log.e("HomeFragment", "Error: ${e.message}")
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private fun setupSwipeGesture() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

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

                    heroCardAdapter.removeHeroAt(position)
                    remainingHeroes.remove(hero)
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
