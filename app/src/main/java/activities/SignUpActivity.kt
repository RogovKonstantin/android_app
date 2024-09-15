package activities

import android.os.Bundle
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.example.andr_dev_application.R

class SignUpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val emailInput: EditText = findViewById(R.id.email_input)
        val usernameInput: EditText = findViewById(R.id.username_input)
        val passwordInput: EditText = findViewById(R.id.password_input)
        val registerButton: MaterialButton = findViewById(R.id.register_button)

        registerButton.setOnClickListener {
            val enteredEmail = emailInput.text.toString().trim()
            val enteredUsername = usernameInput.text.toString().trim()
            val enteredPassword = passwordInput.text.toString().trim()

            if (enteredEmail.isEmpty() || enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(this, "Enter email, username, and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            else{
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()}
        }
    }
}