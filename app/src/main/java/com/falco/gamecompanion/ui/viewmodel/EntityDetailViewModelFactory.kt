package com.falco.gamecompanion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EntityDetailViewModelFactory(
    private val entityId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EntityDetailViewModel::class.java)) {
            return EntityDetailViewModel(entityId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
