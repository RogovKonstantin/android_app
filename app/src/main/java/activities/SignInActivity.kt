package activities

import android.os.Bundle
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.example.andr_dev_application.R

class SignInActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        val loginButton: MaterialButton = findViewById(R.id.login_button)
        val signUpLink: TextView = findViewById(R.id.sign_up_link)


        val receivedUsername = intent.getStringExtra("USERNAME")
        val receivedPassword = intent.getStringExtra("PASSWORD")


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
                Toast.makeText(this, "Enter both email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (enteredEmail == receivedUsername && enteredPassword == receivedPassword) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show()
            }
        }


        signUpLink.setOnClickListener {
            emailInput.text.clear()
            passwordInput.text.clear()

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
