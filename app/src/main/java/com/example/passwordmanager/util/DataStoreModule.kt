package com.example.passwordmanager.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Singleton

/**
 * Preference keys for DataStore
 */
object PreferenceKeys {
    val MASTER_PASSWORD_HASH = stringPreferencesKey("master_password_hash")
    val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
    val AUTO_LOCK_TIMEOUT = intPreferencesKey("auto_lock_timeout")
    val THEME_MODE = stringPreferencesKey("theme_mode")
    val LAST_BACKUP_TIME = longPreferencesKey("last_backup_time")
    val ENCRYPTION_IV = stringPreferencesKey("encryption_iv")
    val PASSWORD_GENERATOR_LENGTH = intPreferencesKey("password_generator_length")
    val PASSWORD_GENERATOR_USE_UPPERCASE = booleanPreferencesKey("password_generator_use_uppercase")
    val PASSWORD_GENERATOR_USE_LOWERCASE = booleanPreferencesKey("password_generator_use_lowercase")
    val PASSWORD_GENERATOR_USE_NUMBERS = booleanPreferencesKey("password_generator_use_numbers")
    val PASSWORD_GENERATOR_USE_SPECIAL = booleanPreferencesKey("password_generator_use_special")
}

/**
 * Extension property for DataStore
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "password_manager_prefs")

/**
 * DataStore module for Hilt dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    /**
     * Provides DataStore instance
     * @param context Application context
     * @return DataStore instance
     */
    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}

/**
 * DataStore repository for handling preferences
 */
class DataStoreRepository(private val dataStore: DataStore<Preferences>) {

    /**
     * Save string value to DataStore
     * @param key Preference key
     * @param value String value to save
     */
    suspend fun saveString(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    /**
     * Get string value from DataStore
     * @param key Preference key
     * @param defaultValue Default value if key doesn't exist
     * @return Flow of string value
     */
    fun getString(key: Preferences.Key<String>, defaultValue: String = ""): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[key] ?: defaultValue
            }
    }

    /**
     * Save boolean value to DataStore
     * @param key Preference key
     * @param value Boolean value to save
     */
    suspend fun saveBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    /**
     * Get boolean value from DataStore
     * @param key Preference key
     * @param defaultValue Default value if key doesn't exist
     * @return Flow of boolean value
     */
    fun getBoolean(key: Preferences.Key<Boolean>, defaultValue: Boolean = false): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[key] ?: defaultValue
            }
    }

    /**
     * Save integer value to DataStore
     * @param key Preference key
     * @param value Integer value to save
     */
    suspend fun saveInt(key: Preferences.Key<Int>, value: Int) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    /**
     * Get integer value from DataStore
     * @param key Preference key
     * @param defaultValue Default value if key doesn't exist
     * @return Flow of integer value
     */
    fun getInt(key: Preferences.Key<Int>, defaultValue: Int = 0): Flow<Int> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[key] ?: defaultValue
            }
    }

    /**
     * Save long value to DataStore
     * @param key Preference key
     * @param value Long value to save
     */
    suspend fun saveLong(key: Preferences.Key<Long>, value: Long) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    /**
     * Get long value from DataStore
     * @param key Preference key
     * @param defaultValue Default value if key doesn't exist
     * @return Flow of long value
     */
    fun getLong(key: Preferences.Key<Long>, defaultValue: Long = 0L): Flow<Long> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[key] ?: defaultValue
            }
    }

    /**
     * Clear all preferences
     */
    suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
