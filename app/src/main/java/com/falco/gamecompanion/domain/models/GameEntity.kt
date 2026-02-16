package com.falco.gamecompanion.domain.models

interface GameEntity {
    val id: String
    val name: String
    val summary: String
    val type: EntityType
    val sourceLinks: List<String>
    val relatedEntityIds: List<String>
}
