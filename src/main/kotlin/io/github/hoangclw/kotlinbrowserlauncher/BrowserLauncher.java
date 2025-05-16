/**
 * KotlinBrowserLauncher - A lightweight cross-platform URL launcher library
 *
 * @author lcaohoanq
 * @version 1.0.0
 * @since 2025-05-16
 *
 * This utility class provides a simple way to open URLs in the system's default web browser.
 * It supports Windows, macOS, and Linux operating systems, with fallback mechanisms
 * when the primary method is not available.
 *
 * <p>Example usage:</p>
 * <pre>
 *     // Open a single URL
 *     BrowserLauncher.openHomePage("https://github.com/lcaohoanq/kotlin-browser-launcher");
 *
 *     // Open multiple URLs
 *     List<String> urls = Arrays.asList(
 *         "https://github.com/lcaohoanq",
 *         "https://github.com/lcaohoanq/kotlin-browser-launcher"
 *     );
 *     BrowserLauncher.openHomePage(urls);
 * </pre>
 *
 * @see java.awt.Desktop
 * @see java.net.URI
 */
package io.github.hoangclw.kotlinbrowserlauncher;

import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Utility class for launching URLs in the system's default web browser.
 * This class handles browser launching across different operating systems,
 * providing fallback mechanisms when Desktop API is not available.
 */
public class BrowserLauncher {

    /**
     * Opens the specified URL(s) in the system's default web browser.
     *
     * @param urls A String containing a single URL or a List of URL strings to open
     * @throws IllegalArgumentException if the argument type is not String or List
     * @throws Exception if any error occurs during the browser launching process
     */
    public static void openHomePage(Object urls) {
        try {
            // Convert input to a list of URL strings
            List<String> urlList = new ArrayList<>();

            // Handle single URL as a string
            if (urls instanceof String) {
                urlList = List.of((String) urls);
            }
            // Handle multiple URLs as a list
            else if (urls instanceof List<?>) {
                for (Object item : (List<?>) urls) {
                    if (item instanceof String) {
                        urlList.add((String) item);
                    }
                }
            }
            // Throw exception for invalid input types
            else {
                throw new IllegalArgumentException("Invalid argument type. Expected String or List<String>");
            }

            // Get Desktop instance if supported by the platform
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

            // Determine the operating system
            String os = System.getProperty("os.name").toLowerCase(Locale.getDefault());

            // Process each URL in the list
            for (String url : urlList) {
                // Primary method: Use Desktop API if available
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(URI.create(url));
                }
                // Fallback methods based on operating system
                else {
                    // Windows-specific approach
                    if (os.contains("win")) {
                        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                    }
                    // macOS-specific approach
                    else if (os.contains("mac")) {
                        Runtime.getRuntime().exec("open " + url);
                    }
                    // Linux/Unix-specific approach
                    else if (os.contains("nix") || os.contains("nux")) {
                        Runtime.getRuntime().exec("xdg-open " + url);
                    }
                    // Unsupported OS
                    else {
                        System.out.println("Unsupported operating system: " + os);
                    }
                }
            }

        } catch (Exception e) {
            // Print stack trace for any exceptions
            e.printStackTrace();
        }
    }
}