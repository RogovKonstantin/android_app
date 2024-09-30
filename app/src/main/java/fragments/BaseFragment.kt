package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.andr_dev_application.R
import com.google.android.material.button.MaterialButton

abstract class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayoutId(), container, false)

        val buttonFunction1: MaterialButton = view.findViewById(R.id.button_function1)
        val buttonFunction2: MaterialButton = view.findViewById(R.id.button_function2)
        val buttonFunction3: MaterialButton = view.findViewById(R.id.button_function3)

        buttonFunction1.setOnClickListener {
            findNavController().navigate(R.id.firstFunctionFragment)
        }

        buttonFunction2.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        buttonFunction3.setOnClickListener {
            findNavController().navigate(R.id.ratedHeroesButtonsFragment)
        }

        return view
    }

    abstract fun getLayoutId(): Int
}