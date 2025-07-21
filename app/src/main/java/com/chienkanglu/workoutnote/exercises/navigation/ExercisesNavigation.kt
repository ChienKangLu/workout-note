package com.chienkanglu.workoutnote.exercises.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.chienkanglu.workoutnote.exercises.ExercisesScreenRoute
import kotlinx.serialization.Serializable

@Serializable
object ExercisesRoute

fun NavController.navigateToExercises(navOptions: NavOptions) = navigate(route = ExercisesRoute, navOptions)

fun NavGraphBuilder.exercisesScreen() {
    composable<ExercisesRoute> {
        ExercisesScreenRoute()
    }
}
