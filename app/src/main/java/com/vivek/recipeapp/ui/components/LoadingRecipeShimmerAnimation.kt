package com.vivek.recipeapp.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerRecipeCardItemRecipeScreen(
    colors: List<Color>,
    cardHeight: Dp,
    padding: Dp,
    xShimmer: Float,
    yShimmer: Float
) {
    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(x = xShimmer - 200, y = yShimmer - 200),
        end = Offset(x = xShimmer, y = yShimmer)
    )

    Column(modifier = Modifier.padding(padding)) {
        Surface(shape = MaterialTheme.shapes.small) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight)
                    .background(brush = brush)
            )
        }

        repeat(5) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(shape = MaterialTheme.shapes.small) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(cardHeight / 10)
                        .background(brush = brush)
                )
            }
        }
    }
}

@Composable
fun RecipeShimmerAnimation(
    padding: Dp = 16.dp,
    cardHeight: Dp,
    colors: List<Color>,
    gradientWidth: Float
) {
    // Animation stuff

    val metrics = LocalContext.current.resources.displayMetrics
    val shimmerEndingX = metrics.widthPixels.toFloat() - (padding.value * 2) + gradientWidth
    val shimmerEndingY = cardHeight.value - padding.value + gradientWidth

    val infiniteTransition = rememberInfiniteTransition()

    val xCardShimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = shimmerEndingX,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1300, delayMillis = 300, easing = LinearEasing)
        )
    )
    val yCardShimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = shimmerEndingY,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1300, delayMillis = 300, easing = LinearEasing)
        )
    )

    // UI stuff

    Column(modifier = Modifier) {
        ShimmerRecipeCardItemRecipeScreen(
            colors = colors,
            cardHeight = cardHeight,
            padding = padding,
            xShimmer = xCardShimmer,
            yShimmer = yCardShimmer
        )
    }
}