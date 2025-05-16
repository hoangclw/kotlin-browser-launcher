/**
 * KotlinBrowserLauncher - A lightweight cross-platform URL launcher library
 *
 * @author hoangclw
 * @version 1.0.0
 * @since 2025-05-16
 *
 * This utility file provides a simple way to open URLs in the system's default web browser.
 * The implementation uses Kotlin's idiomatic features while supporting Windows, macOS, and Linux
 * operating systems with appropriate fallback mechanisms.
 *
 * <p>Example usage:</p>
 * <pre>
 *     // Open a single URL
 *     openHomePage("https://github.com/lcaohoanq/kotlin-browser-launcher")
 *
 *     // Open multiple URLs
 *     val urls = listOf(
 *         "https://github.com/lcaohoanq",
 *         "https://github.com/lcaohoanq/kotlin-browser-launcher"
 *     )
 *     openHomePage(urls)
 * </pre>
 *
 * @see java.awt.Desktop
 * @see java.net.URI
 */
package io.github.hoangclw.kotlinbrowserlauncher

import java.awt.Desktop
import java.net.URI
import java.util.*

/**
 * Opens the specified URL(s) in the system's default web browser.
 * This function handles browser launching across different operating systems,
 * utilizing Kotlin's when expression for more concise and readable code.
 *
 * @param urls A String containing a single URL or a List of URL strings to open
 * @throws IllegalArgumentException if the argument type is not String or List
 * @throws Exception if any error occurs during the browser launching process
 */
fun openHomePage(urls: Any) {
    try {
        // Convert input to a list of URL strings using Kotlin's smart casting and filterIsInstance
        val urlList = when (urls) {
            // If input is a List, filter it to include only String instances
            is List<*> -> urls.filterIsInstance<String>()
            // If input is a String, wrap it in a list
            is String -> listOf(urls)
            // For any other type, throw an exception
            else -> throw IllegalArgumentException("Invalid argument type. Expected String or List<String>")
        }

        // Get Desktop instance if supported by the platform
        val desktop = if (Desktop.isDesktopSupported()) Desktop.getDesktop() else null

        // Determine the operating system using Kotlin's lowercase function with explicit locale
        val os = System.getProperty("os.name").lowercase(Locale.getDefault())

        // Process each URL in the list
        for (url in urlList) {
            // Primary method: Use Desktop API if available
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(URI.create(url))
            }
            // Fallback methods based on operating system using Kotlin's when expression
            else {
                when {
                    // Windows-specific approach
                    os.contains("win") -> Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler $url")
                    // macOS-specific approach
                    os.contains("mac") -> Runtime.getRuntime().exec("open $url")
                    // Linux/Unix-specific approach
                    os.contains("nix") || os.contains("nux") -> Runtime.getRuntime().exec("xdg-open $url")
                    // Unsupported OS
                    else -> println("Unsupported operating system: $os")
                }
            }
        }
    } catch (e: Exception) {
        // Print stack trace for any exceptions
        e.printStackTrace()
    }
}