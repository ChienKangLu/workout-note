package com.chienkanglu.workoutnote.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.chienkanglu.workoutnote.WntAppState
import com.chienkanglu.workoutnote.exercises.navigation.exercisesScreen
import com.chienkanglu.workoutnote.home.navigation.HomeRoute
import com.chienkanglu.workoutnote.home.navigation.homeScreen
import com.chienkanglu.workoutnote.session.navigation.navigateToSession
import com.chienkanglu.workoutnote.session.navigation.sessionScreen

@Composable
fun WntNavHost(
    appState: WntAppState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    NavHost(
        navController,
        startDestination = HomeRoute,
        modifier,
    ) {
        homeScreen(
            onSessionClick = navController::navigateToSession,
        ) {
            sessionScreen(
                onBackClick = navController::popBackStack,
            )
        }
        exercisesScreen()
    }
}
