package com.chienkanglu.workoutnote.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.chienkanglu.workoutnote.R
import com.chienkanglu.workoutnote.exercises.navigation.ExercisesRoute
import com.chienkanglu.workoutnote.home.navigation.HomeRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int,
    val route: KClass<*>,
) {
    HOME(R.string.home, Icons.Default.Home, R.string.home, HomeRoute::class),
    EXERCISES(R.string.exercises, Icons.Default.FitnessCenter, R.string.exercises, ExercisesRoute::class),
}
