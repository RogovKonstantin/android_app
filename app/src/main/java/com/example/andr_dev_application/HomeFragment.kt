package com.example.andr_dev_application

import HeroModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var heroCardAdapter: HeroCardAdapter

    companion object {
        val likedHeroes = mutableListOf<HeroModel>()
        val dislikedHeroes = mutableListOf<HeroModel>()
        val remainingHeroes = mutableListOf<HeroModel>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        fetchUsers()
        setupSwipeGesture()
        return view
    }

    private fun fetchUsers() {
        lifecycleScope.launch {
            try {
                if (remainingHeroes.isEmpty()) {
                    val users: List<HeroModel> = RetrofitInstance.api.getUsers()
                    Toast.makeText(requireContext(), "Fetched ${users.size} users.", Toast.LENGTH_SHORT).show()

                    // Convert List to MutableList and add placeholder
                    remainingHeroes.addAll(users)
                    remainingHeroes.add(HeroModel(0, "", "", "No more heroes nearby", "", "", "", "", true)) // Add placeholder
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
                val position = viewHolder.adapterPosition
                val hero = heroCardAdapter.getHeroAt(position)

                if (hero.isPlaceholder) {
                    // If the card is a placeholder, reset the swipe
                    heroCardAdapter.notifyItemChanged(position)
                    return
                }

                if (direction == ItemTouchHelper.RIGHT) {
                    likeHero(hero)
                } else if (direction == ItemTouchHelper.LEFT) {
                    dislikeHero(hero)
                }

                // Remove the card
                heroCardAdapter.removeHeroAt(position)
                remainingHeroes.remove(hero)
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