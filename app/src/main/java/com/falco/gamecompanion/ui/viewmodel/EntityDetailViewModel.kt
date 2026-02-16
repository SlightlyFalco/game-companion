package com.falco.gamecompanion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falco.gamecompanion.core.Resource
import com.falco.gamecompanion.data.repository.GameRepository
import com.falco.gamecompanion.data.repository.GameRepositoryImpl
import com.falco.gamecompanion.domain.models.GameEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EntityDetailUiState(
    val entity: GameEntity? = null,
    val relatedEntities: Map<String, String> = emptyMap(),
    val resource: Resource<Unit> = Resource.Loading
)

class EntityDetailViewModel(
    entityId: String,
    private val repository: GameRepository = GameRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(EntityDetailUiState())
    val uiState: StateFlow<EntityDetailUiState> = _uiState.asStateFlow()

    init {
        loadEntity(entityId)
    }

    private fun loadEntity(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(resource = Resource.Loading)
            val entity = repository.getEntityById(id)
            if (entity != null) {
                val relatedNames = entity.relatedEntityIds.mapNotNull { relatedId ->
                    repository.getEntityById(relatedId)?.name?.let { relatedId to it }
                }.toMap()
                _uiState.value = EntityDetailUiState(
                    entity = entity,
                    relatedEntities = relatedNames,
                    resource = Resource.Success(Unit)
                )
            } else {
                _uiState.value = _uiState.value.copy(resource = Resource.Error("Not found"))
            }
        }
    }
}
