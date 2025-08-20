package com.example.treetest

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TreeStorage(private val context: Context) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    companion object {
        private val Context.dataStore by preferencesDataStore(name = "tree_prefs")
        private val TREE_KEY = stringPreferencesKey("tree_json")
    }

    val treeJsonFlow: Flow<String?> = dataStore.data
        .map { preferences -> preferences[TREE_KEY] }

    suspend fun saveTreeJson(json: String) {
        dataStore.edit { preferences ->
            preferences[TREE_KEY] = json
        }
    }
}







