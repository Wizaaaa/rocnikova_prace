package com.example.rocnikova_prace.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.shake(isError: Boolean, trigger: Long) = composed {
    val translationX = remember { Animatable(0f) }

    LaunchedEffect(trigger) {
        if (isError) {
            val duration = 30
            val displacement = 8f

            translationX.animateTo(
                targetValue = -displacement,
                animationSpec = tween(durationMillis = duration, easing = LinearEasing)
            )
            translationX.animateTo(
                targetValue = displacement,
                animationSpec = tween(durationMillis = duration, easing = LinearEasing)
            )
            translationX.animateTo(
                targetValue = -displacement,
                animationSpec = tween(durationMillis = duration, easing = LinearEasing)
            )
            translationX.animateTo(
                targetValue = displacement / 2,
                animationSpec = tween(durationMillis = duration, easing = LinearEasing)
            )
            translationX.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = duration, easing = LinearEasing)
            )
        }
    }

    this.graphicsLayer {
        this.translationX = translationX.value
    }
}