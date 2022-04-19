# CustomTabs
Implementation of Chrome Custom Tabs on Android

Follow these steps if you’re looking for the most straightforward implementation of Custom Tabs, no configurations other than setting a color for the toolbar (just for demonstrating that it's possible).

## Steps

1. Add dependency in your `build.gradle (app)` file
    
    ```kotlin
    implementation 'androidx.browser:browser:1.4.0'
    ```
    
2. Add `queries` to the `AndroidManifest.xml` file
    
    ```xml
    <queries>
        <intent>
            <action
                android:name="android.support.customtabs.action.CustomTabsService" />
        </intent>
    </queries>
    ```
    
3. In the Activity/Fragment that will open the Custom Tab, create a `CustomTabsIntent.Builder()`
    
    ```kotlin
    class SimpleFragment : Fragment() {
        // [...]
        private val url: String = "https://www.youtube.com"
    
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
    
            openUrlInCustomTab(url)
        }
    
        private fun openUrlInCustomTab(url: String) {
            val customTabsBuilder = CustomTabsIntent.Builder()
        }
        // [...]
    }
    ```
    
4. Set Custom Tab toolbar color
    
    ```kotlin
    private fun openUrlInCustomTab(url: String) {
        val customTabsBuilder = CustomTabsIntent.Builder()
    
        val greenColor = ContextCompat.getColor(requireContext(), R.color.poatek_green)
        val customToolbarColorParams = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(greenColor)
            .build()
        customTabsBuilder.setDefaultColorSchemeParams(customToolbarColorParams)
    }
    ```
    
5. *(Optional)* Set additional configurations to the builder
    
    ```kotlin
    private fun openUrlInCustomTab(url: String) {
        val customTabsBuilder = CustomTabsIntent.Builder()
    
        /**
         * Show title of the webpage in the toolbar:
         * - customTabsBuilder.setShowTitle(true)
         *
         * To change the "close" button:
         * - customTabsBuilder.setCloseButtonIcon(bitmap)
         *
         * To use animations:
         * - customTabsBuilder.setStartAnimations(
         *      this, android.R.anim.start_in_anim, android.R.anim.start_out_anim
         *   )
         * - customTabsBuilder.setExitAnimations(
         *      this, android.R.anim.exit_in_anim, android.R.anim.exit_out_anim
         *   )
         */
    }
    ```

    | ⚠️ For more options on customizing the Custom Tab, see [Custom Tabs Implementation guide - Chrome Developers](https://developer.chrome.com/docs/android/custom-tabs/integration-guide/). |
    | :--- |

6. Call `builder.build()` when you’re done making changes to the builder
    
    ```kotlin
    private fun openUrlInCustomTab(url: String) {
        val customTabsBuilder = CustomTabsIntent.Builder()
    
        // [...] Customization would go here
    
        val customTabsIntent = customTabsBuilder.build()
    }
    ```
    
7. Check if the device has the Chrome package installed before launching the Custom Tab
    
    ```kotlin
    class SimpleFragment : Fragment() {
        // [...]
        private val isChromePackageInstalled: Boolean
            get() = try {
                // This is why we need that 'queries' section in the AndroidManifest.xml
                requireContext().packageManager.getPackageInfo(CHROME_PACKAGE, 0)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
    
        // [...]
        private fun openUrlInCustomTab(url: String) {
            // [...]
            val customTabsIntent = customTabsBuilder.build()
    
            if (isChromePackageInstalled) {
                // Launch Custom Tabs
            } else {
                // Fallback, open WebView? Open default browser?
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
        }
    }
    ```
    
8. If the package is installed, call `intent.launchUrl(Context, Uri)` to open your beautiful Custom Tab!
    
    ```kotlin
    private fun openUrlInCustomTab(url: String) {
        // [...]
        if (isChromePackageInstalled) {
            customTabsIntent.intent.setPackage(CHROME_PACKAGE)
            customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
        } else {
            // Fallback, open WebView? Launch Intent to any browser? Show error?
        }
    }
    ```
    
9. **PROFIT (or not)!**
