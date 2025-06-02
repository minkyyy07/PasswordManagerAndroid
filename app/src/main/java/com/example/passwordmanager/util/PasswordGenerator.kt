package com.example.passwordmanager.util

import java.security.SecureRandom

/**
 * Password generator utility class
 */
class PasswordGenerator {

    companion object {
        private const val LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz"
        private const val UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        private const val NUMBER_CHARS = "0123456789"
        private const val SPECIAL_CHARS = "!@#$%^&*()_-+=<>?/[]{}|~"
        private const val DEFAULT_LENGTH = 16
        
        private val secureRandom = SecureRandom()
        
        /**
         * Generate a random password
         * @param length Password length
         * @param useUppercase Include uppercase letters
         * @param useLowercase Include lowercase letters
         * @param useNumbers Include numbers
         * @param useSpecial Include special characters
         * @return Generated password
         * @throws IllegalArgumentException if no character sets are selected or length is less than 4
         */
        fun generatePassword(
            length: Int = DEFAULT_LENGTH,
            useUppercase: Boolean = true,
            useLowercase: Boolean = true,
            useNumbers: Boolean = true,
            useSpecial: Boolean = true
        ): String {
            if (length < 4) {
                throw IllegalArgumentException("Password length must be at least 4 characters")
            }
            
            var charPool = ""
            if (useLowercase) charPool += LOWERCASE_CHARS
            if (useUppercase) charPool += UPPERCASE_CHARS
            if (useNumbers) charPool += NUMBER_CHARS
            if (useSpecial) charPool += SPECIAL_CHARS
            
            if (charPool.isEmpty()) {
                throw IllegalArgumentException("At least one character set must be selected")
            }
            
            val password = StringBuilder(length)
            
            // Ensure at least one character from each selected set
            if (useLowercase) password.append(LOWERCASE_CHARS[secureRandom.nextInt(LOWERCASE_CHARS.length)])
            if (useUppercase) password.append(UPPERCASE_CHARS[secureRandom.nextInt(UPPERCASE_CHARS.length)])
            if (useNumbers) password.append(NUMBER_CHARS[secureRandom.nextInt(NUMBER_CHARS.length)])
            if (useSpecial) password.append(SPECIAL_CHARS[secureRandom.nextInt(SPECIAL_CHARS.length)])
            
            // Fill the rest of the password with random characters from the pool
            for (i in password.length until length) {
                val randomIndex = secureRandom.nextInt(charPool.length)
                password.append(charPool[randomIndex])
            }
            
            // Shuffle the password to avoid predictable patterns
            return password.toString().toCharArray().apply { shuffle() }.joinToString("")
        }
        
        /**
         * Generate a memorable password using words and numbers
         * @param wordCount Number of words to include
         * @param includeNumbers Whether to include numbers
         * @param separator Character to separate words
         * @return Generated memorable password
         */
        fun generateMemorablePassword(
            wordCount: Int = 3,
            includeNumbers: Boolean = true,
            separator: String = "-"
        ): String {
            val words = listOf(
                "apple", "banana", "cherry", "dragon", "elephant", "forest", "guitar", "honey",
                "island", "jungle", "kangaroo", "lemon", "mountain", "notebook", "orange", "penguin",
                "queen", "river", "sunset", "tiger", "umbrella", "violin", "window", "xylophone",
                "yellow", "zebra", "airplane", "butterfly", "cactus", "dolphin", "eagle", "flamingo"
            )
            
            val password = StringBuilder()
            
            for (i in 0 until wordCount) {
                if (i > 0) password.append(separator)
                
                val word = words[secureRandom.nextInt(words.size)]
                password.append(word.capitalize())
                
                if (includeNumbers && secureRandom.nextBoolean()) {
                    password.append(secureRandom.nextInt(100))
                }
            }
            
            return password.toString()
        }
        
        /**
         * Calculate password strength on a scale of 0-100
         * @param password Password to evaluate
         * @return Strength score (0-100)
         */
        fun calculatePasswordStrength(password: String): Int {
            if (password.isEmpty()) return 0
            
            var score = 0
            
            // Length contribution (up to 30 points)
            score += minOf(30, password.length * 2)
            
            // Character variety contribution (up to 40 points)
            if (password.any { it in LOWERCASE_CHARS }) score += 10
            if (password.any { it in UPPERCASE_CHARS }) score += 10
            if (password.any { it in NUMBER_CHARS }) score += 10
            if (password.any { it in SPECIAL_CHARS }) score += 10
            
            // Complexity contribution (up to 30 points)
            val uniqueChars = password.toSet().size
            score += minOf(15, uniqueChars)
            
            // Penalize sequential or repeated patterns
            score -= detectPatterns(password)
            
            return maxOf(0, minOf(100, score))
        }
        
        /**
         * Get password strength category based on score
         * @param score Password strength score (0-100)
         * @return Strength category as string
         */
        fun getPasswordStrengthCategory(score: Int): String {
            return when {
                score < 20 -> "Very Weak"
                score < 40 -> "Weak"
                score < 60 -> "Moderate"
                score < 80 -> "Strong"
                else -> "Very Strong"
            }
        }
        
        /**
         * Detect patterns in password and return penalty score
         * @param password Password to check
         * @return Penalty score
         */
        private fun detectPatterns(password: String): Int {
            var penalty = 0
            
            // Check for repeated characters
            val repeatedChars = password.zipWithNext().count { it.first == it.second }
            penalty += repeatedChars * 2
            
            // Check for sequential characters
            val sequentialChars = password.zipWithNext().count { 
                it.second.code - it.first.code == 1 || it.first.code - it.second.code == 1
            }
            penalty += sequentialChars
            
            // Check for keyboard patterns (simplified)
            val keyboardRows = listOf(
                "qwertyuiop",
                "asdfghjkl",
                "zxcvbnm"
            )
            
            for (row in keyboardRows) {
                for (i in 0..row.length - 3) {
                    val pattern = row.substring(i, i + 3)
                    if (password.lowercase().contains(pattern)) {
                        penalty += 5
                    }
                }
            }
            
            return minOf(penalty, 30)
        }
        
        // Default password generation options
        private const val DEFAULT_LENGTH = 16
        private const val DEFAULT_USE_UPPERCASE = true
        private const val DEFAULT_USE_LOWERCASE = true
        private const val DEFAULT_USE_NUMBERS = true
        private const val DEFAULT_USE_SPECIAL = true

        /**
         * Generate a password with default options
         * @return A strong password with default settings
         */
        fun generatePassword(): String {
            return generatePassword(
                length = DEFAULT_LENGTH,
                useUppercase = DEFAULT_USE_UPPERCASE,
                useLowercase = DEFAULT_USE_LOWERCASE,
                useNumbers = DEFAULT_USE_NUMBERS,
                useSpecial = DEFAULT_USE_SPECIAL
            )
        }
    }
    
    /**
     * Extension function to capitalize first letter of a string
     */
    private fun String.capitalize(): String {
        return if (this.isNotEmpty()) this[0].uppercase() + this.substring(1) else this
    }
}
