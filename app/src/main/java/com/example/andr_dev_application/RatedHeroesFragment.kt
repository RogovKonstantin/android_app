package com.example.andr_dev_application

import HeroModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RatedHeroesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var heroCardAdapter: HeroCardAdapter

    companion object {
        private const val ARG_TYPE = "type"

        fun newInstance(type: String): RatedHeroesFragment {
            val fragment = RatedHeroesFragment()
            val args = Bundle()
            args.putString(ARG_TYPE, type)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rated_heroes, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)

        val type = arguments?.getString(ARG_TYPE) ?: "liked"
        displayHeroes(type)

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