package com.chienkanglu.workoutnote.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import kotlin.reflect.KClass

fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false

val NavDestination.routeClassName: String?
    get() =
        route
            ?.substringBefore("?")
            ?.substringBefore("/")
            ?.substringAfterLast(".")
