package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.andr_dev_application.R
import com.google.android.material.button.MaterialButton

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        setupNavigation()
        return binding.root
    }

    private fun setupNavigation() {
        binding.root.findViewById<MaterialButton>(R.id.button_function1)?.setOnClickListener {
            findNavController().navigate(R.id.firstFunctionFragment)
        }
        binding.root.findViewById<MaterialButton>(R.id.button_function2)?.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        binding.root.findViewById<MaterialButton>(R.id.button_function3)?.setOnClickListener {
            findNavController().navigate(R.id.ratedHeroesButtonsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}