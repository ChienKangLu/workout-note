package com.chienkanglu.workoutnote.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chienkanglu.workoutnote.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

fun NavGraphBuilder.homeScreen() {
    composable<HomeRoute> {
        HomeScreen()
    }
}
