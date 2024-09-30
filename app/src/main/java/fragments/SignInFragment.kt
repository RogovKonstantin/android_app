package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.andr_dev_application.R
import com.google.android.material.button.MaterialButton

class SignInFragment : Fragment() {

    private val validEmail = "q"
    private val validPassword = "q"

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        emailInput = view.findViewById(R.id.email_input)
        passwordInput = view.findViewById(R.id.password_input)
        val loginButton: MaterialButton = view.findViewById(R.id.login_button)
        val signUpLink: TextView = view.findViewById(R.id.sign_up_link)

        val receivedUsername = arguments?.getString("USERNAME")
        val receivedPassword = arguments?.getString("PASSWORD")

        receivedUsername?.let {
            emailInput.setText(it)
        }
        receivedPassword?.let {
            passwordInput.setText(it)
        }

        loginButton.setOnClickListener {
            val enteredEmail = emailInput.text.toString().trim()
            val enteredPassword = passwordInput.text.toString().trim()

            if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(activity, "Enter both email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (enteredEmail == receivedUsername && enteredPassword == receivedPassword || enteredEmail == validEmail && enteredPassword == validPassword) {
                Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
            } else {
                Toast.makeText(activity, "Invalid Email or Password", Toast.LENGTH_SHORT).show()
            }
        }

        signUpLink.setOnClickListener {
            emailInput.text.clear()
            passwordInput.text.clear()
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        return view
    }
}