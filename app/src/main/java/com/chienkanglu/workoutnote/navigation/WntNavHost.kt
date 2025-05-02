package com.chienkanglu.workoutnote.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.chienkanglu.workoutnote.home.navigation.HomeRoute
import com.chienkanglu.workoutnote.home.navigation.homeScreen

@Composable
fun WntNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController,
        startDestination = HomeRoute,
        modifier,
    ) {
        homeScreen()
    }
}
