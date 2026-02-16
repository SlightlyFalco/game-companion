package com.falco.gamecompanion.ui.navigation

import com.falco.gamecompanion.domain.models.EntityType

sealed class GameRoute(val route: String) {
    data object Home : GameRoute("home")
    data class EntityList(val type: EntityType) : GameRoute("entity_list/${type.name}") {
        companion object {
            const val routeWithArg = "entity_list/{entityType}"
            fun createRoute(type: EntityType) = "entity_list/${type.name}"
            fun fromRoute(route: String): EntityType? =
                EntityType.entries.find { "entity_list/${it.name}" == route }
        }
    }
    data class EntityDetail(val entityId: String) : GameRoute("entity_detail/$entityId") {
        companion object {
            const val routeWithArg = "entity_detail/{entityId}"
            fun createRoute(entityId: String) = "entity_detail/$entityId"
        }
    }
}
