package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.andr_dev_application.R
import com.google.android.material.button.MaterialButton

class SignUpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        val emailInput: EditText = view.findViewById(R.id.email_input)
        val usernameInput: EditText = view.findViewById(R.id.username_input)
        val passwordInput: EditText = view.findViewById(R.id.password_input)
        val registerButton: MaterialButton = view.findViewById(R.id.register_button)

        registerButton.setOnClickListener {
            val enteredEmail = emailInput.text.toString().trim()
            val enteredUsername = usernameInput.text.toString().trim()
            val enteredPassword = passwordInput.text.toString().trim()

            if (enteredEmail.isEmpty() || enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(activity, "Enter email, username, and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bundle = Bundle().apply {
                putString("USERNAME", enteredUsername)
                putString("PASSWORD", enteredPassword)
            }
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment, bundle)
        }

        return view
    }
}