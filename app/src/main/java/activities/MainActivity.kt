package activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.andr_dev_application.R

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        val buttonFunction1: Button = findViewById(R.id.button_function1)
        val buttonFunction2: Button = findViewById(R.id.button_function2)
        val buttonFunction3: Button = findViewById(R.id.button_function3)

        buttonFunction1.setOnClickListener {
            navController.navigate(R.id.firstFunctionFragment)
        }

        buttonFunction2.setOnClickListener {
            navController.navigate(R.id.homeFragment)
        }

        buttonFunction3.setOnClickListener {
            navController.navigate(R.id.ratedHeroesButtonsFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}