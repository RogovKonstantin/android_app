package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.andr_dev_application.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private val validEmail = "q"
    private val validPassword = "q"
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val args: SignInFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root

        args.userCredentials?.let {
            binding.emailInput.setText(it.email)
            binding.passwordInput.setText(it.password)
        }

        binding.loginButton.setOnClickListener {
            val enteredEmail = binding.emailInput.text.toString().trim()
            val enteredPassword = binding.passwordInput.text.toString().trim()

            if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(activity, "Enter both email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if ((enteredEmail == args.userCredentials?.email && enteredPassword == args.userCredentials!!.password) ||
                (enteredEmail == validEmail && enteredPassword == validPassword)) {
                Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToHomeFragment())
            } else {
                Toast.makeText(activity, "Invalid Email or Password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signUpLink.setOnClickListener {
            binding.emailInput.text.clear()
            binding.passwordInput.text.clear()
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}