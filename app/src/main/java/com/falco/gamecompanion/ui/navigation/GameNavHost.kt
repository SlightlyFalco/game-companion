package com.falco.gamecompanion.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.falco.gamecompanion.ui.screens.EntityDetailScreen
import com.falco.gamecompanion.ui.screens.EntityListScreen
import com.falco.gamecompanion.ui.screens.HomeScreen

@Composable
fun GameNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = GameRoute.Home.route
    ) {
        composable(GameRoute.Home.route) {
            HomeScreen(
                onNavigateToEntityList = { type ->
                    navController.navigate(GameRoute.EntityList.createRoute(type))
                }
            )
        }
        composable(
            route = GameRoute.EntityList.routeWithArg,
            arguments = listOf(navArgument("entityType") { type = NavType.StringType })
        ) { backStackEntry ->
            val typeName = backStackEntry.arguments?.getString("entityType")
            val type = typeName?.let { name ->
                com.falco.gamecompanion.domain.models.EntityType.entries.find { it.name == name }
            }
            if (type != null) {
                EntityListScreen(
                    entityType = type,
                    onEntityClick = { id ->
                        navController.navigate(GameRoute.EntityDetail.createRoute(id))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable(
            route = GameRoute.EntityDetail.routeWithArg,
            arguments = listOf(navArgument("entityId") { type = NavType.StringType })
        ) { backStackEntry ->
            val entityId = backStackEntry.arguments?.getString("entityId") ?: return@composable
            EntityDetailScreen(
                entityId = entityId,
                onRelatedEntityClick = { id ->
                    navController.navigate(GameRoute.EntityDetail.createRoute(id)) {
                        popUpTo(GameRoute.EntityDetail.createRoute(entityId)) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
