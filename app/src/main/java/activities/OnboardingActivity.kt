package activities

import android.os.Bundle
import com.google.android.material.button.MaterialButton
import android.content.Intent
import com.example.andr_dev_application.R

class OnboardingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val nextButton: MaterialButton = findViewById(R.id.next_button)
        nextButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
