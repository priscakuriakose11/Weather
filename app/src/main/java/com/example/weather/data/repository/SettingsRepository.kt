package com.example.weather.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SettingsRepository(context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("units")
    private val mDataStore: DataStore<Preferences> = context.dataStore

    companion object {
        val PREFERENCE_UNIT_KEY = stringPreferencesKey("PREFERENCE_UNIT")
    }

    suspend fun saveCurrentUnit(unit: String) {
        mDataStore.edit { preferences ->
            preferences[PREFERENCE_UNIT_KEY] = unit

        }
    }

    val unitFlow: Flow<String> = mDataStore.data.map {
        it[PREFERENCE_UNIT_KEY] ?: "Metric"
    }


}


