package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.andr_dev_application.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import utils.HeroCardAdapter
import utils.HeroModel
import utils.StackLayoutManager
import utils.api.RetrofitInstance

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }


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
        val view = super.onCreateView(inflater, container, savedInstanceState) as View
        fetchHeroes()
        setupSwipeGesture()
        return view
    }

    private fun fetchHeroes() {
        lifecycleScope.launch {
            try {
                if (remainingHeroes.isEmpty()) {
                    val heroes: List<HeroModel> = RetrofitInstance.api.getUsers()
                    Toast.makeText(requireContext(), "Fetched ${heroes.size} users.", Toast.LENGTH_SHORT).show()

                    remainingHeroes.addAll(heroes)
                    remainingHeroes.add(HeroModel(0, "", "", "", "", "", "", "", true))
                }
                heroCardAdapter = HeroCardAdapter(remainingHeroes)
                binding.recyclerView.adapter = heroCardAdapter
                binding.recyclerView.layoutManager = StackLayoutManager(requireContext())
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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
}