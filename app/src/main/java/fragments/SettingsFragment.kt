package fragments
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.andr_dev_application.databinding.ButtonsNavBinding
import com.example.andr_dev_application.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    // SharedPreferences to store theme and language settings
    private lateinit var sharedPreferences: SharedPreferences
    private val THEME_PREFERENCE = "theme_pref"
    private val IS_DARK_MODE = "is_dark_mode"
    private val LANGUAGE_PREFERENCE = "language_pref"
    private val SELECTED_LANGUAGE = "selected_language"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(THEME_PREFERENCE, 0)

        // Setup the theme switch functionality
        setupThemeSwitch()

        // Setup language selection
        setupLanguageSpinner()

        // Setup navigation buttons
        setupNavigation()

        return binding.root
    }

    private fun setupThemeSwitch() {

            // Set switch to reflect the saved theme preference
            val isDarkMode = sharedPreferences.getBoolean(IS_DARK_MODE, false)
            binding.themeSwitch.isChecked = isDarkMode

            // Apply the theme immediately if preference exists
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )

            // Add listener to toggle theme
            binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
                val editor = sharedPreferences.edit()
                editor.putBoolean(IS_DARK_MODE, isChecked)
                editor.apply()

                // Change the app's theme based on the switch
                AppCompatDelegate.setDefaultNightMode(
                    if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                )
            }

    }

    private fun setupLanguageSpinner() {

            // Language options
            val languages = arrayOf("English", "Русский")

            // Setup spinner adapter
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.languageSpinner.adapter = adapter

            // Set the currently selected language
            val selectedLanguage = sharedPreferences.getString(SELECTED_LANGUAGE, "English")
            binding.languageSpinner.setSelection(languages.indexOf(selectedLanguage))

            // Add listener to change language on selection
            binding.languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val newLanguage = languages[position]
                    if (newLanguage != selectedLanguage) {
                        val editor = sharedPreferences.edit()
                        editor.putString(SELECTED_LANGUAGE, newLanguage)
                        editor.apply()
                        // Reload the activity to apply language change
                        Log.d("SettingsFragment", "Language changed to $newLanguage, recreating activity")
                        requireActivity().recreate()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

    }

    private fun setupNavigation() {

            val navBinding = ButtonsNavBinding.bind(binding.root)
            navBinding.buttonFunction2.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.actionFirstFunctionFragmentToHomeFragment())
            }
            navBinding.buttonFunction3.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.actionFirstFunctionFragmentToRatedHeroesButtonsFragment())
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}