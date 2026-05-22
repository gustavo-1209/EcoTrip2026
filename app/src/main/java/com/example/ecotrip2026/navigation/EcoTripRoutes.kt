package com.example.ecotrip2026.navigation

import kotlinx.serialization.Serializable

@Serializable
object FormularioRoute

@Serializable
data class ResumenRoute(
    val nombre: String,
    val destino: String,
    val diasDuracion: Int,
    val tipoTransporte: String,
    val huellaCarbonoActiva: Boolean
)
