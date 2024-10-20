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
import kotlinx.coroutines.Job
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

    private var themeSwitchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().getSharedPreferences(LANGUAGE_PREFERENCE, 0)
        themePreferences = ThemePreferences(requireContext())

        checkFileAndBackupExistence()

        binding.deleteFileButton.setOnClickListener {
            deleteFile()
        }

        binding.restoreFileButton.setOnClickListener {
            restoreFileFromInternalStorage()
        }

        setupThemeSwitch()
        setupLanguageSpinner()
        setupNavigation()

        return binding.root
    }

    private fun setupThemeSwitch() {
        themeSwitchJob = lifecycleScope.launch {
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

        binding.languageSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val newLanguage = languages[position]
                    if (newLanguage != selectedLanguage) {
                        sharedPreferences.edit().putString(SELECTED_LANGUAGE, newLanguage).apply()
                        Log.d("SettingsFragment", "Language changed to $newLanguage")
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
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

    private fun checkFileAndBackupExistence() {
        val fileName = "heroes.txt"
        val externalFile =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
        val internalFile = File(requireContext().filesDir, fileName)

        if (FileUtils.fileExists(externalFile)) {
            binding.fileStatusTextView.setText(R.string.file_exists)
            binding.deleteFileButton.visibility = View.VISIBLE
            binding.restoreFileButton.visibility = View.GONE
        } else {
            binding.fileStatusTextView.setText(R.string.file_does_not_exist)
            binding.deleteFileButton.visibility = View.GONE
            if (FileUtils.fileExists(internalFile)) {
                binding.backupFileStatusTextView.setText(R.string.backup_file_exists)
                binding.restoreFileButton.visibility = View.VISIBLE
            } else {
                binding.backupFileStatusTextView.setText(R.string.backup_file_does_not_exist)
                binding.restoreFileButton.visibility = View.GONE
            }
        }
    }

    private fun deleteFile() {
        val fileName = "heroes.txt"
        val externalFile =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
        val internalFile = File(requireContext().filesDir, fileName)

        if (FileUtils.fileExists(externalFile)) {
            if (FileUtils.copyFile(externalFile, internalFile)) {
                if (FileUtils.deleteFile(externalFile)) {
                    Toast.makeText(
                        requireContext(),
                        "File moved to internal storage",
                        Toast.LENGTH_SHORT
                    ).show()
                    checkFileAndBackupExistence()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to delete file from external storage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Failed to move file to internal storage",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(requireContext(), "File does not exist", Toast.LENGTH_SHORT).show()
        }
    }

    private fun restoreFileFromInternalStorage() {
        val fileName = "heroes.txt"
        val internalFile = File(requireContext().filesDir, fileName)
        val externalFile =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

        if (FileUtils.fileExists(internalFile)) {
            if (FileUtils.copyFile(internalFile, externalFile)) {
                if (FileUtils.deleteFile(internalFile)) {
                    Toast.makeText(
                        requireContext(),
                        "File restored to external storage",
                        Toast.LENGTH_SHORT
                    ).show()
                    checkFileAndBackupExistence()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to delete file from internal storage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Failed to restore file to external storage",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(requireContext(), "Backup file does not exist", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onStop() {
        super.onStop()
        themeSwitchJob?.cancel()
    }

}
