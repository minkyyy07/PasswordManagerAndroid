package com.example.passwordmanager.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Copy text to clipboard
 * @param context Application context
 * @param label Label for the clip
 * @param text Text to be copied
 */
fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}

/**
 * Copy text to clipboard and show a toast message
 * @param context Application context
 * @param label Label for the clip
 * @param text Text to be copied
 * @param toastMessage Message to show in toast
 */
fun copyToClipboardWithToast(context: Context, label: String, text: String, toastMessage: String) {
    copyToClipboard(context, label, text)
    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
}

/**
 * Get text from clipboard
 * @param context Application context
 * @return The text from clipboard or null if clipboard is empty or doesn't contain text
 */
fun getTextFromClipboard(context: Context): String? {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    if (!clipboard.hasPrimaryClip()) return null
    
    val item = clipboard.primaryClip?.getItemAt(0)
    return item?.text?.toString()
}

/**
 * Clear clipboard after specified delay
 * @param context Application context
 * @param delayMillis Delay in milliseconds before clearing
 */
fun clearClipboardAfterDelay(context: Context, delayMillis: Long = 60000) {
    CoroutineScope(Dispatchers.Main).launch {
        delay(delayMillis)
        clearClipboard(context)
    }
}

/**
 * Clear clipboard content
 * @param context Application context
 */
fun clearClipboard(context: Context) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        clipboard.clearPrimaryClip()
    } else {
        // For older Android versions
        val clip = ClipData.newPlainText("", "")
        clipboard.setPrimaryClip(clip)
    }
}

/**
 * Check if clipboard has content
 * @param context Application context
 * @return True if clipboard has content, false otherwise
 */
fun hasClipboardContent(context: Context): Boolean {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return clipboard.hasPrimaryClip()
}

/**
 * Add clipboard change listener
 * @param context Application context
 * @param listener Listener to be called when clipboard content changes
 * @return The clipboard manager instance
 */
fun addClipboardChangeListener(context: Context, listener: ClipboardManager.OnPrimaryClipChangedListener): ClipboardManager {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.addPrimaryClipChangedListener(listener)
    return clipboard
}

/**
 * Remove clipboard change listener
 * @param context Application context
 * @param listener Listener to be removed
 */
fun removeClipboardChangeListener(context: Context, listener: ClipboardManager.OnPrimaryClipChangedListener) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.removePrimaryClipChangedListener(listener)
}