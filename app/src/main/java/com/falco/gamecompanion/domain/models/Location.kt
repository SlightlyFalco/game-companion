package com.falco.gamecompanion.domain.models

data class Location(
    override val id: String,
    override val name: String,
    override val summary: String,
    override val sourceLinks: List<String>,
    override val relatedEntityIds: List<String>
) : GameEntity {
    override val type: EntityType = EntityType.LOCATION
}
