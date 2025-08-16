package com.chienkanglu.workoutnote.session.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.chienkanglu.workoutnote.session.SessionScreenRoute
import com.chienkanglu.workoutnote.session.SessionViewModel
import kotlinx.serialization.Serializable

@Serializable
data class SessionRoute(
    val sessionId: Int,
)

fun NavController.navigateToSession(
    sessionId: Int,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(route = SessionRoute(sessionId)) {
    navOptions()
}

fun NavGraphBuilder.sessionScreen(onBackClick: () -> Unit) {
    composable<SessionRoute> { entry ->
        val sessionId = entry.toRoute<SessionRoute>().sessionId
        SessionScreenRoute(
            onBackClick = onBackClick,
            viewModel =
                hiltViewModel<SessionViewModel, SessionViewModel.Factory>(
                    key = sessionId.toString(),
                ) { factory ->
                    factory.create(sessionId)
                },
        )
    }
}
