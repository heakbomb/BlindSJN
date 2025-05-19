package com.glowstudio.android.blindsjn.data.session

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userDataStore by preferencesDataStore(name = "user_prefs")

object UserPrefsKeys {
    val USER_ID = intPreferencesKey("user_id")
}

suspend fun saveUserId(context: Context, userId: Int) {
    context.userDataStore.edit { prefs ->
        prefs[UserPrefsKeys.USER_ID] = userId
    }
}

fun getUserIdFlow(context: Context): Flow<Int> =
    context.userDataStore.data.map { prefs -> prefs[UserPrefsKeys.USER_ID] ?: -1 }

suspend fun clearUserId(context: Context) {
    context.userDataStore.edit { prefs ->
        prefs.remove(UserPrefsKeys.USER_ID)
    }
} 