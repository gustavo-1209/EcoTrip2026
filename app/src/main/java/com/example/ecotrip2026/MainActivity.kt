package com.example.ecotrip2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ecotrip2026.navigation.EcoTripNavHost
import com.example.ecotrip2026.ui.theme.EcoTrip2026Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EcoTrip2026Theme {
                EcoTripNavHost()
            }
        }
    }
}
