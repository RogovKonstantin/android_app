package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.andr_dev_application.databinding.FragmentRatedHeroesBinding
import utils.HeroCardAdapter
import utils.HeroModel

class RatedHeroesFragment : Fragment() {

    private var _binding: FragmentRatedHeroesBinding? = null
    private val binding get() = _binding!!
    private lateinit var heroCardAdapter: HeroCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRatedHeroesBinding.inflate(inflater, container, false)
        val view = binding.root

        val args = RatedHeroesFragmentArgs.fromBundle(requireArguments())
        setupRecyclerView(args.type)

        return view
    }


    private fun setupRecyclerView(type: String) {
        val heroes = getHeroesForType(type)
        heroCardAdapter = HeroCardAdapter(heroes.toMutableList())
        binding.recyclerView.adapter = heroCardAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getHeroesForType(type: String): List<HeroModel> {
        return when (type) {
            "liked" -> HomeFragment.likedHeroes
            "disliked" -> HomeFragment.dislikedHeroes
            else -> emptyList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
