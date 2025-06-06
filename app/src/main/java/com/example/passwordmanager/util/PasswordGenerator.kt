package com.example.passwordmanager.util

import kotlin.random.Random

object PasswordGenerator {
    private const val UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz"
    private const val NUMBER_CHARS = "0123456789"
    private const val SPECIAL_CHARS = "!@#$%^&*()-_=+[]{}|;:,.<>?/"

    fun generate(
        length: Int = 12,
        includeUppercase: Boolean = true,
        includeLowercase: Boolean = true,
        includeNumbers: Boolean = true,
        includeSpecialChars: Boolean = false
    ): String {
        val charPool = StringBuilder()
        if (includeUppercase) charPool.append(UPPERCASE_CHARS)
        if (includeLowercase) charPool.append(LOWERCASE_CHARS)
        if (includeNumbers) charPool.append(NUMBER_CHARS)
        if (includeSpecialChars) charPool.append(SPECIAL_CHARS)

        if (charPool.isEmpty()) {
            charPool.append(LOWERCASE_CHARS) // Default to lowercase if no options selected
        }

        return (1..length)
            .map { Random.nextInt(0, charPool.length) }
            .map(charPool::get)
            .joinToString("")
    }
}

fun calculateStrength(password: String): Float {
    if (password.isEmpty()) return 0f

    var score = 0f

    score += minOf(password.length / 20f, 1f) * 40f

    val hasLowercase = password.any { it.isLowerCase() }
    val hasUppercase = password.any { it.isUpperCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecial = password.any { !it.isLetterOrDigit() }

    if (hasLowercase) score += 15f
    if (hasUppercase) score += 15f
    if (hasDigit) score += 15f
    if (hasSpecial) score += 15f

    return score / 100f
}
