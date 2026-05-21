package com.example.ecotrip2026.model

import kotlinx.serialization.Serializable

@Serializable
data class PreferenciaViaje(
    val nombre: String,
    val tipoTransporte: String,
    val huellaCarbonoActiva: Boolean
)