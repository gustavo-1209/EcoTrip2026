package com.example.ecotrip2026.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ecotrip2026.model.PreferenciaViaje
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para asegurar que solo exista una instancia del archivo de preferencias
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ecotrip_preferences")

class DataStoreManager(private val context: Context) {

    // Definición de las claves de almacenamiento
    companion object {
        private val KEY_NOMBRE = stringPreferencesKey("user_name")
        private val KEY_TIPO_TRANSPORTE = stringPreferencesKey("transport_type")
        private val KEY_HUELLA_CARBONO = booleanPreferencesKey("carbon_footprint_active")
    }

    // Flujo de lectura: Transforma los datos en disco al objeto PreferenciaViaje
    val preferenciaViajeFlow: Flow<PreferenciaViaje> = context.dataStore.data.map { preferences ->
        val nombre = preferences[KEY_NOMBRE] ?: ""
        val tipoTransporte = preferences[KEY_TIPO_TRANSPORTE] ?: ""
        val huellaCarbono = preferences[KEY_HUELLA_CARBONO] ?: false

        PreferenciaViaje(nombre, tipoTransporte, huellaCarbono)
    }

    // Función asíncrona de escritura
    suspend fun guardarPreferenciaViaje(preferencia: PreferenciaViaje) {
        context.dataStore.edit { preferences ->
            preferences[KEY_NOMBRE] = preferencia.nombre
            preferences[KEY_TIPO_TRANSPORTE] = preferencia.tipoTransporte
            preferences[KEY_HUELLA_CARBONO] = preferencia.huellaCarbonoActiva
        }
    }
}