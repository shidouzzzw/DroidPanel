package com.droidpanel.app.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.droidpanel.app.model.DiagModule
import com.droidpanel.app.ui.dashboard.DashboardScreen
import com.droidpanel.app.ui.detail.ModuleDetailScreen

object Routes {
    const val DASHBOARD = "dashboard"
    const val MODULE = "module/{id}"
    fun module(id: String) = "module/$id"
}

@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    NavHost(
        navController = nav,
        startDestination = Routes.DASHBOARD,
        enterTransition = { fadeIn(tween(220)) + slideInHorizontally(tween(260)) { it / 10 } },
        exitTransition = { fadeOut(tween(160)) },
        popEnterTransition = { fadeIn(tween(220)) },
        popExitTransition = { fadeOut(tween(160)) + slideOutHorizontally(tween(240)) { it / 10 } },
    ) {
        composable(Routes.DASHBOARD) {
            DashboardScreen(onOpenModule = { nav.navigate(Routes.module(it.id)) })
        }
        composable(
            route = Routes.MODULE,
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
        ) { entry ->
            val module = DiagModule.fromId(entry.arguments?.getString("id"))
            if (module == null) {
                LaunchedEffect(Unit) { nav.popBackStack() }
            } else {
                ModuleDetailScreen(module = module, onBack = { nav.popBackStack() })
            }
        }
    }
}
