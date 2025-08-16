package com.chienkanglu.workoutnote.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.chienkanglu.workoutnote.home.HomeScreenRoute
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(route = HomeRoute, navOptions)

fun NavGraphBuilder.homeScreen(
    onSessionClick: (Int) -> Unit,
    sessionDestination: NavGraphBuilder.() -> Unit,
) {
    composable<HomeRoute> {
        HomeScreenRoute(onSessionClick)
    }
    sessionDestination()
}
