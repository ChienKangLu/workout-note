package com.chienkanglu.workoutnote

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chienkanglu.workoutnote.navigation.WntNavHost
import com.chienkanglu.workoutnote.navigation.isRouteInHierarchy
import com.chienkanglu.workoutnote.ui.common.isCompact

@Composable
fun WntApp(appState: WntAppState) {
    val currentDestination = appState.currentDestination

    val adaptiveInfo = currentWindowAdaptiveInfo()

    val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()

    LaunchedEffect(key1 = currentDestination, key2 = adaptiveInfo) {
        appState.navController.addOnDestinationChangedListener { _, destination, _ ->
            if (adaptiveInfo.isCompact()) {
                appState.updateNavigationVisibility(destination)
            }
        }
    }

    LaunchedEffect(appState.isNavigationVisible) {
        if (appState.isNavigationVisible) {
            navigationSuiteScaffoldState.show()
        } else {
            navigationSuiteScaffoldState.hide()
        }
    }

    NavigationSuiteScaffold(
        state = navigationSuiteScaffoldState,
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
