package com.example.andr_dev_application

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {

    private lateinit var buttonFunction1: Button
    private lateinit var buttonFunction2: Button
    private lateinit var buttonFunction3: Button

    private var firstFunctionFragment: Fragment? = null
    private var homeFragment: Fragment? = null
    private var ratedHeroesButtonsFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonFunction1 = findViewById(R.id.button_function1)
        buttonFunction2 = findViewById(R.id.button_function2)
        buttonFunction3 = findViewById(R.id.button_function3)

        buttonFunction2.isSelected = true
        buttonFunction2.scaleX = 1.2f
        buttonFunction2.scaleY = 1.2f

        buttonFunction1.setOnClickListener {
            selectButton(buttonFunction1)
            showFragment(FirstFunctionFragment::class.java)
        }

        buttonFunction2.setOnClickListener {
            selectButton(buttonFunction2)
            showFragment(HomeFragment::class.java)
        }

        buttonFunction3.setOnClickListener {
            selectButton(buttonFunction3)
            showFragment(RatedHeroesButtonsFragment::class.java)
        }

        // Load the HomeFragment by default
        showFragment(HomeFragment::class.java)
    }

    private fun selectButton(selectedButton: Button) {
        val buttons = listOf(buttonFunction1, buttonFunction2, buttonFunction3)
        buttons.forEach { button ->
            button.isSelected = button == selectedButton
            button.scaleX = if (button.isSelected) 1.2f else 1.0f
            button.scaleY = if (button.isSelected) 1.2f else 1.0f
        }
    }

    private fun showFragment(fragmentClass: Class<out Fragment>) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // Hide all fragments
        fragmentManager.fragments.forEach { fragmentTransaction.hide(it) }

        // Show the selected fragment
        val tag = fragmentClass.simpleName
        var fragment = fragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = fragmentClass.newInstance()
            fragmentTransaction.add(R.id.fragment_container, fragment, tag)
        } else {
            fragmentTransaction.show(fragment)
        }

        fragmentTransaction.commit()
    }
}