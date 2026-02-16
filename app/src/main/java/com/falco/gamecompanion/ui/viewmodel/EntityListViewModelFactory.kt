package com.falco.gamecompanion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.falco.gamecompanion.domain.models.EntityType

class EntityListViewModelFactory(
    private val entityType: EntityType
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EntityListViewModel::class.java)) {
            return EntityListViewModel(entityType) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
