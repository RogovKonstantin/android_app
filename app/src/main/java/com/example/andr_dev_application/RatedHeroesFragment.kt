package com.example.andr_dev_application

import HeroModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RatedHeroesFragment : Fragment() {

    private lateinit var likedButton: Button
    private lateinit var dislikedButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var heroCardAdapter: HeroCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rated_heroes, container, false)
        likedButton = view.findViewById(R.id.liked_button)
        dislikedButton = view.findViewById(R.id.disliked_button)
        recyclerView = view.findViewById(R.id.recyclerView)

        likedButton.setOnClickListener { displayHeroes("liked") }
        dislikedButton.setOnClickListener { displayHeroes("disliked") }

        return view
    }

    private fun displayHeroes(type: String) {
        val heroes: List<HeroModel> = if (type == "liked") {
            HomeFragment.likedHeroes
        } else {
            HomeFragment.dislikedHeroes
        }

        heroCardAdapter = HeroCardAdapter(heroes.toMutableList())
        recyclerView.adapter = heroCardAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}