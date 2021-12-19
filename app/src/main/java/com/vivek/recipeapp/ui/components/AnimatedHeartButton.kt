package com.vivek.recipeapp.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vivek.recipeapp.ui.components.HeartButtonState.ACTIVE
import com.vivek.recipeapp.ui.components.HeartButtonState.IDLE

enum class HeartButtonState {
    IDLE,
    ACTIVE
}

@Composable
fun AnimatedHeartButton(
    buttonState: HeartButtonState,
    onToggle: () -> Unit
) {
    val idleIconSize = 40.dp
    val activeIconSize = 50.dp

    val transition = updateTransition(
        targetState = buttonState,
        label = "Heart Animation"
    )

    val heartSize by transition.animateDp(
        label = "Size",
        transitionSpec = {
            when {
                IDLE isTransitioningTo ACTIVE -> keyframes {
                    durationMillis = 500
                    activeIconSize at 150
                    idleIconSize at 300
                }
                else -> keyframes {
                    durationMillis = 500
                    activeIconSize at 150
                    idleIconSize at 300
                }
            }
        }
    ) { state ->
        when (state) {
            IDLE -> idleIconSize
            ACTIVE -> idleIconSize
        }
    }

    val heartColor by transition.animateColor(
        label = "Color",
        transitionSpec = {
            when {
                IDLE isTransitioningTo ACTIVE -> tween(durationMillis = 500)
                else -> tween(durationMillis = 500)
            }
        }
    ) { state ->
        when (state) {
            IDLE -> Color.LightGray
            ACTIVE -> Color.Red
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Icon(
            imageVector = Icons.Rounded.Favorite,
            contentDescription = "Heart Icon",
            tint = heartColor,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(heartSize)
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    onToggle()
                }
                .align(Alignment.CenterHorizontally)
        )
    }
}




























