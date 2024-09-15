package activities

import android.os.Bundle
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.TextView
import com.example.andr_dev_application.R

class SignInActivity : BaseActivity() {

    private val validEmail = "q"
    private val validPassword = "q"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val emailInput: EditText = findViewById(R.id.email_input)
        val passwordInput: EditText = findViewById(R.id.password_input)
        val loginButton: MaterialButton = findViewById(R.id.login_button)
        val signUpLink: TextView = findViewById(R.id.sign_up_link)

        loginButton.setOnClickListener {
            val enteredEmail = emailInput.text.toString().trim()
            val enteredPassword = passwordInput.text.toString().trim()

            if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(this, "Enter both email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (enteredEmail == validEmail && enteredPassword == validPassword) {
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
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}