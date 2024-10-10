package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.andr_dev_application.databinding.ButtonsNavBinding
import com.example.andr_dev_application.databinding.FragmentFirstFunctionBinding

class FirstFunctionFragment : Fragment() {

    private var _binding: FragmentFirstFunctionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstFunctionBinding.inflate(inflater, container, false)
        setupNavigation()
        return binding.root
    }

    private fun setupNavigation() {
        val navBinding = ButtonsNavBinding.bind(binding.root)
        navBinding.buttonFunction2.setOnClickListener {
            findNavController().navigate(FirstFunctionFragmentDirections.actionFirstFunctionFragmentToHomeFragment())
        }
        navBinding.buttonFunction3.setOnClickListener {
            findNavController().navigate(FirstFunctionFragmentDirections.actionFirstFunctionFragmentToRatedHeroesButtonsFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}