package com.alexs.repository.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alexs.domain.repository.UserDataStoreRepository
import com.alexs.repository.utils.Constants.USER_AUTH_TOKEN_KEY
import com.alexs.repository.utils.Constants.USER_EMAIL_KEY
import com.alexs.repository.utils.Constants.USER_NAME_KEY
import com.alexs.repository.utils.Constants.USER_PHONE_KEY
import com.alexs.repository.utils.datastore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserDataStoreImpl @Inject constructor(
    @ApplicationContext context: Context
) : UserDataStoreRepository {

    private object PreferencesKeys {
        val userNameKey = stringPreferencesKey(name = USER_NAME_KEY)
        val userAuthToken = stringPreferencesKey(name = USER_AUTH_TOKEN_KEY)
        val userEmailKey = stringPreferencesKey(name = USER_EMAIL_KEY)
        val userPhoneKey = stringPreferencesKey(name = USER_PHONE_KEY)
    }

    private val dataStore = context.datastore

    override suspend fun persistUserName(userName: String) {
        dataStore.edit { preference ->
            preference[PreferencesKeys.userNameKey] = userName
        }
    }

    override suspend fun persistAuthToken(authToken: String) {
        dataStore.edit { preference ->
            preference[PreferencesKeys.userAuthToken] = authToken
        }
    }

    override suspend fun persistUserEmail(userEmail: String) {
        dataStore.edit { preference ->
            preference[PreferencesKeys.userEmailKey] = userEmail
        }
    }

    override suspend fun persistUserPhone(userPhone: String) {
        dataStore.edit { preference ->
            preference[PreferencesKeys.userPhoneKey] = userPhone
        }
    }

    override val readUserName: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preference ->
            preference[PreferencesKeys.userNameKey] ?: ""
        }

    override val readAuthToken: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preference ->
            preference[PreferencesKeys.userAuthToken]
        }

    override val readUserEmail: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preference ->
            preference[PreferencesKeys.userEmailKey] ?: ""
        }

    override val readUserPhone: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preference ->
            preference[PreferencesKeys.userPhoneKey] ?: ""
        }

}