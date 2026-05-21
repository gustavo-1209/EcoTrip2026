package com.example.ecotrip2026.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ecotrip2026.data.DataStoreManager

object EcoTripViewModelFactory {

    val Factory = viewModelFactory {
        initializer {
            val application = this[APPLICATION_KEY] as Application

            EcoTripViewModel(
                savedStateHandle = createSavedStateHandle(),
                dataStoreManager = DataStoreManager(application.applicationContext)
            )
        }
    }
}
