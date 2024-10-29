package fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.andr_dev_application.R
import com.example.andr_dev_application.databinding.ButtonsNavBinding
import com.example.andr_dev_application.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch
import utils.FileUtils
import utils.ThemePreferences
import java.io.File

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private val LANGUAGE_PREFERENCE = "language_pref"
    private val SELECTED_LANGUAGE = "selected_language"

    private lateinit var themePreferences: ThemePreferences

    private val FILE_NAME = "heroes.txt"
    private val EXTERNAL_DIR = Environment.DIRECTORY_DOCUMENTS

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().getSharedPreferences(LANGUAGE_PREFERENCE, 0)
        themePreferences = ThemePreferences(requireContext())

        checkFileAndBackupExistence()
        setupThemeSwitch()
        setupLanguageSpinner()
        setupNavigation()

        binding.deleteFileButton.setOnClickListener {
            val externalFile = File(requireContext().getExternalFilesDir(EXTERNAL_DIR), FILE_NAME)
            val internalFile = File(requireContext().filesDir, FILE_NAME)
            moveFileToInternalStorage(externalFile, internalFile)
        }

        binding.restoreFileButton.setOnClickListener {
            val externalFile = File(requireContext().getExternalFilesDir(EXTERNAL_DIR), FILE_NAME)
            val internalFile = File(requireContext().filesDir, FILE_NAME)
            restoreFileToExternalStorage(internalFile, externalFile)
        }

        return binding.root
    }

    private fun setupThemeSwitch() {

        lifecycleScope.launch {
            themePreferences.isDarkMode.collect { isDarkMode ->
                if (isAdded) {
                    binding.themeSwitch.isChecked = isDarkMode
                    AppCompatDelegate.setDefaultNightMode(
                        if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                    )
                }
            }
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                if (isAdded) {
                    themePreferences.setDarkMode(isChecked)
                }
            }
        }
    }

    private fun setupLanguageSpinner() {
        val languages = arrayOf("en", "ru")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            arrayOf("English", "Русский")
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.languageSpinner.adapter = adapter

        val selectedLanguage = sharedPreferences.getString(SELECTED_LANGUAGE, "en")
        binding.languageSpinner.setSelection(languages.indexOf(selectedLanguage))

        binding.languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val newLanguage = languages[position]
                if (newLanguage != selectedLanguage) {
                    sharedPreferences.edit().putString(SELECTED_LANGUAGE, newLanguage).apply()
                    Log.d("SettingsFragment", "Language changed to $newLanguage")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }



    private fun checkFileAndBackupExistence() {
        val externalFile = File(requireContext().getExternalFilesDir(EXTERNAL_DIR), FILE_NAME)
        val internalFile = File(requireContext().filesDir, FILE_NAME)

        val isFileExists = FileUtils.fileExists(externalFile)
        val isBackupExists = FileUtils.fileExists(internalFile)

        binding.deleteFileButton.visibility = if (isFileExists) View.VISIBLE else View.GONE
        binding.restoreFileButton.visibility = if (!isFileExists && isBackupExists) View.VISIBLE else View.GONE

        updateFileStatusViews(isFileExists, isBackupExists)
    }

    private fun updateFileStatusViews(fileExists: Boolean, backupExists: Boolean) {
        binding.fileStatusTextView.setText(if (fileExists) R.string.file_exists else R.string.file_does_not_exist)
        binding.backupFileStatusTextView.setText(if (backupExists) R.string.backup_file_exists else R.string.backup_file_does_not_exist)
    }

    private fun moveFileToInternalStorage(file: File, backup: File) {
        if (FileUtils.fileExists(file) && FileUtils.copyFile(file, backup) && FileUtils.deleteFile(file)) {
            showToast("File moved to internal storage")
            checkFileAndBackupExistence()
        } else {
            showToast("Failed to move file")
        }
    }

    private fun restoreFileToExternalStorage(backup: File, file: File) {
        if (FileUtils.fileExists(backup) && FileUtils.copyFile(backup, file) && FileUtils.deleteFile(backup)) {
            showToast("File restored to external storage")
            checkFileAndBackupExistence()
        } else {
            showToast("Failed to restore file")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setupNavigation() {
        val navBinding = ButtonsNavBinding.bind(binding.root)
        navBinding.buttonFunction2.setOnClickListener {
            if (isAdded) {
                findNavController().navigate(SettingsFragmentDirections.actionFirstFunctionFragmentToHomeFragment())
            }
        }
        navBinding.buttonFunction3.setOnClickListener {
            if (isAdded) {
                findNavController().navigate(SettingsFragmentDirections.actionFirstFunctionFragmentToRatedHeroesButtonsFragment())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
