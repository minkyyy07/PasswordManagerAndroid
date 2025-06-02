package com.example.passwordmanager.util

import android.content.Context
import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * Extension to show toast message
 * @param message Message to show
 * @param duration Toast duration, default is short
 */
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

/**
 * Extension to show toast message from resource
 * @param resId String resource id
 * @param duration Toast duration, default is short
 */
fun Context.showToast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, resId, duration).show()
}

/**
 * Extension to show snackbar
 * @param message Message to show
 * @param duration Snackbar duration, default is short
 */
fun View.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message, duration).show()
}

/**
 * Extension to show snackbar with action
 * @param message Message to show
 * @param actionText Text for action button
 * @param action Action to perform when button is clicked
 * @param duration Snackbar duration, default is short
 */
fun View.showSnackbarWithAction(
    message: String,
    actionText: String,
    action: () -> Unit,
    duration: Int = Snackbar.LENGTH_LONG
) {
    Snackbar.make(this, message, duration)
        .setAction(actionText) { action() }
        .show()
}

/**
 * Extension to hide keyboard
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Extension to show keyboard
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * Extension to set visibility to VISIBLE
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * Extension to set visibility to INVISIBLE
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Extension to set visibility to GONE
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * Extension to toggle visibility between VISIBLE and GONE
 */
fun View.toggleVisibility() {
    visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
}

/**
 * Extension to add text change listener to EditText
 * @param afterTextChanged Function to call after text changes
 * @return TextWatcher instance
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit): TextWatcher {
    val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    }
    this.addTextChangedListener(watcher)
    return watcher
}

/**
 * Extension to set error on TextInputLayout
 * @param message Error message or null to clear error
 */
fun TextInputLayout.setErrorMessage(message: String?) {
    this.error = message
    this.isErrorEnabled = message != null
}

/**
 * Extension to convert dp to pixels
 * @param dp Value in dp
 * @return Value in pixels
 */
fun Context.dpToPx(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    ).roundToInt()
}

/**
 * Extension to convert pixels to dp
 * @param px Value in pixels
 * @return Value in dp
 */
fun Context.pxToDp(px: Int): Float {
    return px / resources.displayMetrics.density
}

/**
 * Extension to format date
 * @param pattern Date format pattern
 * @return Formatted date string
 */
fun Date.format(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(this)
}

/**
 * Extension to parse string to date
 * @param dateString Date string
 * @param pattern Date format pattern
 * @return Date object or null if parsing fails
 */
fun String.toDate(pattern: String = "yyyy-MM-dd HH:mm:ss"): Date? {
    return try {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        dateFormat.parse(this)
    } catch (e: Exception) {
        null
    }
}

/**
 * Extension to get current date
 * @return Current date
 */
fun getCurrentDate(): Date {
    return Calendar.getInstance().time
}

/**
 * Extension to observe LiveData once
 * @param owner LifecycleOwner
 * @param observer Observer function
 */
fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner, object : Observer<T> {
        override fun onChanged(value: T) {
            removeObserver(this)
            observer(value)
        }
    })
}

/**
 * Extension to post value safely on MutableLiveData
 * @param value Value to post
 */
fun <T> MutableLiveData<T>.postSafe(value: T) {
    if (Thread.currentThread() != Thread.currentThread()) {
        postValue(value)
    } else {
        setValue(value)
    }
}

/**
 * Extension to get screen width in pixels
 * @return Screen width
 */
val screenWidth: Int
    get() = Resources.getSystem().displayMetrics.widthPixels

/**
 * Extension to get screen height in pixels
 * @return Screen height
 */
val screenHeight: Int
    get() = Resources.getSystem().displayMetrics.heightPixels
