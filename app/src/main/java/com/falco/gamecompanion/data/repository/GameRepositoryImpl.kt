package com.falco.gamecompanion.data.repository

import com.falco.gamecompanion.core.Result
import com.falco.gamecompanion.data.remote.FakeRemoteDataSource
import com.falco.gamecompanion.domain.models.EntityType
import com.falco.gamecompanion.domain.models.GameEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameRepositoryImpl(
    private val remote: FakeRemoteDataSource = FakeRemoteDataSource()
) : GameRepository {

    override fun getAllEntities(): Flow<List<GameEntity>> =
        remote.getAllEntities().map { result ->
            when (result) {
                is Result.Success -> result.data
                is Result.Error -> emptyList()
            }
        }

    override suspend fun getEntityById(id: String): GameEntity? {
        return when (val result = remote.getEntityById(id)) {
            is Result.Success -> result.data
            is Result.Error -> null
        }
    }

    override suspend fun getEntitiesByType(type: EntityType): List<GameEntity> {
        return when (val result = remote.getEntitiesByType(type)) {
            is Result.Success -> result.data
            is Result.Error -> emptyList()
        }
    }
}
