package fragments

import utils.HeroModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import utils.HeroCardAdapter
import com.example.andr_dev_application.R
import utils.StackLayoutManager
import kotlinx.coroutines.launch
import utils.api.RetrofitInstance

class HomeFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }

        val likedHeroes = mutableListOf<HeroModel>()
        val dislikedHeroes = mutableListOf<HeroModel>()
        val remainingHeroes = mutableListOf<HeroModel>()
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var heroCardAdapter: HeroCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        recyclerView = view!!.findViewById(R.id.recyclerView)
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
                recyclerView.adapter = heroCardAdapter
                recyclerView.layoutManager = StackLayoutManager(requireContext())
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
        itemTouchHelper.attachToRecyclerView(recyclerView)
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