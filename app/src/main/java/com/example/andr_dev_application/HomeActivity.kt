package com.example.andr_dev_application

import HeroModel
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var heroCardAdapter: HeroCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewPager = findViewById(R.id.viewPager)

        fetchUsers()
    }

    private fun fetchUsers() {
        lifecycleScope.launch {
            try {
                val users: List<HeroModel> = RetrofitInstance.api.getUsers()
                Toast.makeText(this@HomeActivity, "Fetched ${users.size} users.", Toast.LENGTH_SHORT).show()
                heroCardAdapter = HeroCardAdapter(users)
                viewPager.adapter = heroCardAdapter
            } catch (e: Exception) {
                Toast.makeText(this@HomeActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
