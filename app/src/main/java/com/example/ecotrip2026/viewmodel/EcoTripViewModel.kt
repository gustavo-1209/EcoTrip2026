package com.example.ecotrip2026.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecotrip2026.data.DataStoreManager
import com.example.ecotrip2026.model.PreferenciaViaje
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EcoTripUiState(
    val nombre: String = "",
    val destino: String = "",
    val diasDuracion: String = "",
    val tipoTransporte: String = "Tren",
    val huellaCarbonoActiva: Boolean = false,
    val mensajeError: String? = null
)

class EcoTripViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        EcoTripUiState(
            nombre = savedStateHandle["nombre"] ?: "",
            destino = savedStateHandle["destino"] ?: "",
            diasDuracion = savedStateHandle["diasDuracion"] ?: "",
            tipoTransporte = savedStateHandle["tipoTransporte"] ?: "Tren",
            huellaCarbonoActiva = savedStateHandle["huellaCarbonoActiva"] ?: false
        )
    )

    val uiState: StateFlow<EcoTripUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreManager.preferenciaViajeFlow.collect { preferencia ->
                _uiState.value = _uiState.value.copy(
                    nombre = preferencia.nombre,
                    tipoTransporte = preferencia.tipoTransporte.ifBlank { "Tren" },
                    huellaCarbonoActiva = preferencia.huellaCarbonoActiva
                )

                savedStateHandle["nombre"] = preferencia.nombre
                savedStateHandle["tipoTransporte"] = preferencia.tipoTransporte.ifBlank { "Tren" }
                savedStateHandle["huellaCarbonoActiva"] = preferencia.huellaCarbonoActiva
            }
        }
    }

    fun actualizarNombre(valor: String) {
        _uiState.value = _uiState.value.copy(nombre = valor, mensajeError = null)
        savedStateHandle["nombre"] = valor
        guardarPreferenciasGlobales()
    }

    fun actualizarDestino(valor: String) {
        _uiState.value = _uiState.value.copy(destino = valor, mensajeError = null)
        savedStateHandle["destino"] = valor
    }

    fun actualizarDiasDuracion(valor: String) {
        val valorLimpio = valor.filter { it.isDigit() }
        _uiState.value = _uiState.value.copy(diasDuracion = valorLimpio, mensajeError = null)
        savedStateHandle["diasDuracion"] = valorLimpio
    }

    fun actualizarTipoTransporte(valor: String) {
        _uiState.value = _uiState.value.copy(tipoTransporte = valor, mensajeError = null)
        savedStateHandle["tipoTransporte"] = valor
        guardarPreferenciasGlobales()
    }

    fun actualizarHuellaCarbono(valor: Boolean) {
        _uiState.value = _uiState.value.copy(huellaCarbonoActiva = valor, mensajeError = null)
        savedStateHandle["huellaCarbonoActiva"] = valor
        guardarPreferenciasGlobales()
    }

    fun validarFormulario(): Boolean {
        val estado = _uiState.value
        val dias = estado.diasDuracion.toIntOrNull()

        return when {
            estado.nombre.isBlank() -> {
                mostrarError("Ingrese el nombre del viajero")
                false
            }

            estado.destino.isBlank() -> {
                mostrarError("Ingrese el destino del viaje")
                false
            }

            dias == null || dias <= 0 -> {
                mostrarError("Ingrese una duración válida en días")
                false
            }

            else -> true
        }
    }

    private fun mostrarError(mensaje: String) {
        _uiState.value = _uiState.value.copy(mensajeError = mensaje)
    }

    private fun guardarPreferenciasGlobales() {
        val estado = _uiState.value

        viewModelScope.launch {
            dataStoreManager.guardarPreferenciaViaje(
                PreferenciaViaje(
                    nombre = estado.nombre,
                    tipoTransporte = estado.tipoTransporte,
                    huellaCarbonoActiva = estado.huellaCarbonoActiva
                )
            )
        }
    }
}
