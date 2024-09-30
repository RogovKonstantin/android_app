package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.andr_dev_application.databinding.FragmentRatedHeroesButtonsBinding

class RatedHeroesButtonsFragment : BaseFragment<FragmentRatedHeroesButtonsBinding>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentRatedHeroesButtonsBinding {
        return FragmentRatedHeroesButtonsBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState) as View
        binding.likedButton.setOnClickListener { navigateToRatedHeroesFragment("liked") }
        binding.dislikedButton.setOnClickListener { navigateToRatedHeroesFragment("disliked") }
        return view
    }

    private fun navigateToRatedHeroesFragment(type: String) {
        val action = RatedHeroesButtonsFragmentDirections
            .actionRatedHeroesButtonsFragmentToRatedHeroesFragment(type)
        findNavController().navigate(action)
    }
}