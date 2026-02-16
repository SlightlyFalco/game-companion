package com.falco.gamecompanion.data.repository

import com.falco.gamecompanion.domain.models.EntityType
import com.falco.gamecompanion.domain.models.GameEntity
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getAllEntities(): Flow<List<GameEntity>>
    suspend fun getEntityById(id: String): GameEntity?
    suspend fun getEntitiesByType(type: EntityType): List<GameEntity>
}
