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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import utils.HeroCardAdapter
import utils.HeroModel
import utils.StackLayoutManager
import utils.api.RetrofitInstance
import utils.FileUtils
import utils.api.HeroApiRepository
import utils.RepositoryProvider
import utils.db.HeroDbRepository

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val remainingHeroes = mutableListOf<HeroModel>()

    companion object {
        val likedHeroes = mutableListOf<HeroModel>()
        val dislikedHeroes = mutableListOf<HeroModel>()
    }

    private lateinit var heroCardAdapter: HeroCardAdapter
    private lateinit var heroDbRepository: HeroDbRepository
    private lateinit var heroApiRepository: HeroApiRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        heroDbRepository = RepositoryProvider.provideHeroDbRepository(requireContext())
        heroApiRepository = RepositoryProvider.provideHeroApiRepository(RetrofitInstance.api)

        setupNavigation()
        observeHeroes()
        setupSwipeGesture()
        setupFetchHeroesButton()

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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                heroDbRepository.fetchHeroesFromLocal().collect { heroes ->
                    if (heroes.isEmpty()) {
                        fetchAndSaveHeroes()
                    } else {
                        updateHeroList(heroes)
                    }
                }
            }
        }
    }

    private fun fetchAndSaveHeroes() {
        lifecycleScope.launch {
            try {
                val apiHeroes = heroApiRepository.fetchHeroes()
                heroDbRepository.saveHeroesToLocal(apiHeroes)
                saveHeroesToFile(apiHeroes)
                updateHeroList(heroDbRepository.fetchHeroesFromLocal().first())
            } catch (e: Exception) {
                showError("Error fetching heroes: ${e.message}")
            }
        }
    }

    private fun updateHeroList(heroes: List<HeroModel>) {
        remainingHeroes.clear()
        remainingHeroes.addAll(heroes)
        setupHeroRecyclerView(remainingHeroes)
    }

    private fun saveHeroesToFile(heroes: List<HeroModel>) {
        val heroesFile = FileUtils.saveHeroesToFile(requireContext(), heroes)
        showToast(
            if (heroesFile != null)
                "Heroes saved to ${heroesFile.absolutePath}"
            else
                "Failed to save heroes"
        )
    }

    private fun setupHeroRecyclerView(heroes: List<HeroModel>) {
        heroCardAdapter = HeroCardAdapter(heroes.toMutableList()).apply { ensurePlaceholder() }
        binding.recyclerView.adapter = heroCardAdapter
        binding.recyclerView.layoutManager = StackLayoutManager(requireContext())
    }

    private fun setupSwipeGesture() {
        val swipeHandler = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
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

                    handleHeroSwipe(hero, direction)
                    heroCardAdapter.removeHeroAt(position)
                }
            }
        }

        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.recyclerView)
    }

    private fun handleHeroSwipe(hero: HeroModel, direction: Int) {
        if (direction == ItemTouchHelper.RIGHT) {
            likeHero(hero)
        } else if (direction == ItemTouchHelper.LEFT) {
            dislikeHero(hero)
        }

        lifecycleScope.launch { heroDbRepository.deleteHero(hero) }
    }

    private fun likeHero(hero: HeroModel) {
        likedHeroes.add(hero)
        showToast("Liked ${hero.firstName}")
    }

    private fun dislikeHero(hero: HeroModel) {
        dislikedHeroes.add(hero)
        showToast("Disliked ${hero.firstName}")
    }

    private fun setupFetchHeroesButton() {
        binding.fetchHeroesButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val apiHeroes = heroApiRepository.fetchHeroes()
                    val localHeroes = heroDbRepository.fetchHeroesFromLocal().first()
                    val newHeroes = apiHeroes.filter { it.id !in localHeroes.map { it.id } }

                    if (newHeroes.isNotEmpty()) {
                        heroDbRepository.saveHeroesToLocal(newHeroes)
                        updateHeroList(heroDbRepository.fetchHeroesFromLocal().first())
                        showToast("New heroes added to the database!")
                    } else {
                        showToast("No new heroes to add.")
                    }
                } catch (e: Exception) {
                    showError("Error fetching or saving heroes: ${e.message}")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Log.e("HomeFragment", message)
        showToast("Error: $message")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        lifecycleScope.coroutineContext.cancel()
    }
}
