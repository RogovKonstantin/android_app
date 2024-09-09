package com.example.andr_dev_application

import HeroModel
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var heroCardAdapter: HeroCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recyclerView)

        fetchUsers()

        setupSwipeGesture()
    }

    private fun fetchUsers() {
        lifecycleScope.launch {
            try {
                val users: List<HeroModel> = RetrofitInstance.api.getUsers()
                Toast.makeText(this@HomeActivity, "Fetched ${users.size} users.", Toast.LENGTH_SHORT).show()

                // Convert List to MutableList
                heroCardAdapter = HeroCardAdapter(users.toMutableList())
                recyclerView.adapter = heroCardAdapter
                recyclerView.layoutManager = LinearLayoutManager(this@HomeActivity)
            } catch (e: Exception) {
                Toast.makeText(this@HomeActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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

                if (direction == ItemTouchHelper.RIGHT) {
                    likeHero(hero)
                } else if (direction == ItemTouchHelper.LEFT) {
                    dislikeHero(hero)
                }

                // Remove the card
                heroCardAdapter.removeHeroAt(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun likeHero(hero: HeroModel) {
        Toast.makeText(this, "Liked ${hero.firstName}", Toast.LENGTH_SHORT).show()
    }

    private fun dislikeHero(hero: HeroModel) {
        Toast.makeText(this, "Disliked ${hero.firstName}", Toast.LENGTH_SHORT).show()
    }
}
