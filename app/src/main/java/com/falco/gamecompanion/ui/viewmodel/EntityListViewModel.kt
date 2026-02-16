package com.falco.gamecompanion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falco.gamecompanion.core.Resource
import com.falco.gamecompanion.data.repository.GameRepository
import com.falco.gamecompanion.data.repository.GameRepositoryImpl
import com.falco.gamecompanion.domain.models.EntityType
import com.falco.gamecompanion.domain.models.GameEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EntityListUiState(
    val entities: List<GameEntity> = emptyList(),
    val filteredEntities: List<GameEntity> = emptyList(),
    val searchQuery: String = "",
    val resource: Resource<Unit> = Resource.Loading
)

class EntityListViewModel(
    val entityType: EntityType,
    private val repository: GameRepository = GameRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(EntityListUiState())
    val uiState: StateFlow<EntityListUiState> = _uiState.asStateFlow()

    init {
        loadEntities()
    }

    fun setSearchQuery(query: String) {
        _uiState.update { state ->
            val filtered = if (query.isBlank()) state.entities
            else state.entities.filter {
                it.name.contains(query, ignoreCase = true) ||
                    it.summary.contains(query, ignoreCase = true)
            }
            state.copy(
                searchQuery = query,
                filteredEntities = filtered
            )
        }
    }

    private fun loadEntities() {
        viewModelScope.launch {
            _uiState.update { it.copy(resource = Resource.Loading) }
            val list = repository.getEntitiesByType(entityType)
            _uiState.update {
                it.copy(
                    entities = list,
                    filteredEntities = list,
                    resource = Resource.Success(Unit)
                )
            }
        }
    }
}
