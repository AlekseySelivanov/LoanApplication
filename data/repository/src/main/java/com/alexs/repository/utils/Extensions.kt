package com.alexs.repository.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = Constants.DATASTORE_NAME)