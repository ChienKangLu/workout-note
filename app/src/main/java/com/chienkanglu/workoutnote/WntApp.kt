package com.chienkanglu.workoutnote

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.chienkanglu.workoutnote.navigation.WntNavHost
import kotlin.reflect.KClass

@Composable
fun WntApp(appState: WntAppState) {
    val currentDestination = appState.currentDestination

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            appState.topLevelDestinations.forEach { destination ->
                val selected =
                    currentDestination
                        .isRouteInHierarchy(destination.route)
                item(
                    icon = {
                        Icon(
                            destination.icon,
                            contentDescription = stringResource(destination.contentDescription),
                        )
                    },
                    label = { Text(stringResource(destination.label)) },
                    selected = selected,
                    onClick = { appState.navigateToTopLevelDestination(destination) },
                )
            }
        },
    ) {
        Scaffold { innerPadding ->
            WntNavHost(appState, modifier = Modifier.padding(innerPadding))
        }
    }
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false
