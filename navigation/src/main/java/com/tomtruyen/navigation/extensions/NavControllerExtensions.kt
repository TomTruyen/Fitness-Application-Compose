package com.tomtruyen.navigation.extensions

import androidx.navigation.NavBackStackEntry

fun NavBackStackEntry?.getScreenRoute() = this?.destination?.route?.split("?")?.firstOrNull()
    ?: this?.destination?.route

