package com.example.ecotrip2026.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ecotrip2026.ui.screens.FormularioScreen
import com.example.ecotrip2026.viewmodel.EcoTripViewModel
import com.example.ecotrip2026.viewmodel.EcoTripViewModelFactory

@Composable
fun EcoTripNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: EcoTripViewModel = viewModel(factory = EcoTripViewModelFactory.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = FormularioRoute
    ) {
        composable<FormularioRoute> {
            FormularioScreen(
                uiState = uiState,
                onNombreChange = viewModel::actualizarNombre,
                onDestinoChange = viewModel::actualizarDestino,
                onDiasDuracionChange = viewModel::actualizarDiasDuracion,
                onTipoTransporteChange = viewModel::actualizarTipoTransporte,
                onHuellaCarbonoChange = viewModel::actualizarHuellaCarbono,
                onContinuar = {
                    if (viewModel.validarFormulario()) {
                        val estadoActual = viewModel.uiState.value
                        navController.navigate(
                            ResumenRoute(
                                nombre = estadoActual.nombre.trim(),
                                tipoTransporte = estadoActual.tipoTransporte,
                                huellaCarbonoActiva = estadoActual.huellaCarbonoActiva
                            )
                        ) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable<ResumenRoute> { backStackEntry ->
            val datos = backStackEntry.toRoute<ResumenRoute>()

            Scaffold { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "Resumen del viaje")
                    Text(text = "Viajero: ${datos.nombre}")
                    Text(text = "Transporte: ${datos.tipoTransporte}")
                    Text(
                        text = "Ruta baja en carbono: ${
                            if (datos.huellaCarbonoActiva) "Sí" else "No"
                        }"
                    )

                    Button(
                        onClick = {
                            navController.navigate(FormularioRoute) {
                                popUpTo<FormularioRoute> {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Text("Volver al formulario")
                    }
                }
            }
        }
    }
}
