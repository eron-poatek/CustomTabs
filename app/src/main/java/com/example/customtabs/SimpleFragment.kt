package com.example.customtabs

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.customtabs.databinding.FragmentSimpleBinding

private const val CHROME_PACKAGE = "com.android.chrome"

class SimpleFragment : Fragment() {

    private var _binding: FragmentSimpleBinding? = null
    private val binding: FragmentSimpleBinding
        get() = _binding!!

    /**
     * Check if user has Chrome installed or not.
     */
    private val isChromePackageInstalled: Boolean
        get() = try {
            requireContext().packageManager.getPackageInfo(CHROME_PACKAGE, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    override fun onResume() {
        super.onResume()
        binding.simpleUrlField.focusAndShowKeyboard()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSimpleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Button is clickable only if user has added something in this field
        binding.simpleUrlField.addTextChangedListener {
            binding.simpleOpenCustomTabButton.isEnabled = !it.isNullOrBlank()
        }

        binding.simpleUrlField.onDone { getUrlAndOpenCustomTab() }
        binding.simpleOpenCustomTabButton.setOnClickListener { getUrlAndOpenCustomTab() }

        // Just to check if view should be enabled or not
        binding.simpleUrlField.setText(binding.simpleUrlField.text?.toString().orEmpty())
    }

    private fun getUrlAndOpenCustomTab() {
        val valueFromUrlField = binding.simpleUrlField.text?.toString()

        if (!valueFromUrlField.isNullOrBlank()) {
            openUrlInCustomTab(valueFromUrlField)
        }
    }

    /**
     * Opens specified URL using Chrome Custom Tabs.
     *
     * OTHER OPTIONS FOR BUILDER:
     *
     * Show title of the webpage in the toolbar:
     * - customTabsBuilder.setShowTitle(true)
     *
     * To change the "close" button:
     * - customTabsBuilder.setCloseButtonIcon(bitmap)
     *
     * To use animations:
     * - customTabsBuilder.setStartAnimations(this, android.R.anim.start_in_anim, android.R.anim.start_out_anim)
     * - customTabsBuilder.setExitAnimations(this, android.R.anim.exit_in_anim, android.R.anim.exit_out_anim)
     */
    private fun openUrlInCustomTab(url: String) {
        val customTabsBuilder = CustomTabsIntent.Builder()

        // Set the toolbar color using CustomTabColorSchemeParams
        val customToolbarColorParams = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(ContextCompat.getColor(requireContext(), R.color.poatek_green))
            .build()

        customTabsBuilder.setDefaultColorSchemeParams(customToolbarColorParams)

        val customTabsIntent = customTabsBuilder.build()

        val validUri = Uri.parse(getValidUrl(url))
        if (isChromePackageInstalled) {
            customTabsIntent.intent.setPackage(CHROME_PACKAGE)
            customTabsIntent.launchUrl(requireContext(), validUri)
        } else {
            // Fallback, opening default browser
            startActivity(Intent(Intent.ACTION_VIEW, validUri))
        }
    }

    @Suppress("ConvertToStringTemplate")
    private fun getValidUrl(url: String): String {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "https://" + url
        }
        return url
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
