package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.andr_dev_application.R

class RatedHeroesButtonsFragment : Fragment() {
    companion object {
        fun newInstance(): RatedHeroesButtonsFragment {
            return RatedHeroesButtonsFragment()
        }
    }

    private lateinit var likedButton: Button
    private lateinit var dislikedButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rated_heroes_buttons, container, false)
        likedButton = view.findViewById(R.id.liked_button)
        dislikedButton = view.findViewById(R.id.disliked_button)

        likedButton.setOnClickListener { navigateToRatedHeroesFragment("liked") }
        dislikedButton.setOnClickListener { navigateToRatedHeroesFragment("disliked") }

        return view
    }

    private fun navigateToRatedHeroesFragment(type: String) {
        val action = RatedHeroesButtonsFragmentDirections
            .actionRatedHeroesButtonsFragmentToRatedHeroesFragment(type)
        findNavController().navigate(action)
    }
}