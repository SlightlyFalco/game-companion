package com.falco.gamecompanion.domain.models

data class Quest(
    override val id: String,
    override val name: String,
    override val summary: String,
    override val sourceLinks: List<String>,
    override val relatedEntityIds: List<String>,
    val prerequisites: List<String>,
    val steps: List<String>,
    val rewards: List<String>
) : GameEntity {
    override val type: EntityType = EntityType.QUEST
}
