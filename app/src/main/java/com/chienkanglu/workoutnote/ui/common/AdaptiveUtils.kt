package com.chienkanglu.workoutnote.ui.common

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.window.core.layout.WindowSizeClass

fun WindowAdaptiveInfo.isCompact() =
    !windowSizeClass.isAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND,
        WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND,
    )
