package `in`.silive.felix.datastore

import `in`.silive.felix.datastore.DataStoreManager.PreferenceKey.logInState
import `in`.silive.felix.datastore.DataStoreManager.PreferenceKey.token
import `in`.silive.felix.module.LogInInfo
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class DataStoreManager(val context: Context) {

    companion object PreferenceKey{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "datastore")
        val token = stringPreferencesKey("token")
        val logInState = booleanPreferencesKey("logInState")
    }

    suspend fun storeLogInInfo(logInInfo: LogInInfo){
        context.dataStore.edit {
            it[token] = logInInfo.token
            it[logInState] = logInInfo.logInState
        }
    }

    suspend fun getLogInInfo() = context.dataStore.data.map {
        LogInInfo(
            token = it[token]?:"",
            logInState = it[logInState]?:false
        )
    }
}