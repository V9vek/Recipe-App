package com.vivek.recipeapp.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vivek.recipeapp.ui.theme.Blue800
import com.vivek.recipeapp.ui.theme.RedErrorLight

enum class PulseState {
    Initial,
    Final
}

@Composable
fun PulsingAnimation() {
    var pulseState by remember { mutableStateOf(PulseState.Initial) }

    // infinite transition
    val infiniteTransition = rememberInfiniteTransition()
    val colorInfinite = infiniteTransition.animateColor(
        initialValue = Blue800,
        targetValue = RedErrorLight,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val sizeInfinite = infiniteTransition.animateValue(
        initialValue = 40.dp,
        targetValue = 50.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    //

    // simple transition
    val transition = updateTransition(
        targetState = pulseState,
        label = "Pulse Transition"
    )

    val color by transition.animateColor(label = "Color") { state ->
        when (state) {
            PulseState.Initial -> Blue800
            PulseState.Final -> RedErrorLight
        }
    }

    val size by transition.animateDp(label = "Size") { state ->
        when (state) {
            PulseState.Initial -> 16.dp
            PulseState.Final -> 32.dp
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = {
                pulseState = if (pulseState == PulseState.Initial) PulseState.Final
                else PulseState.Initial
            }
        ) {
            Text(text = "Pulse")
        }

        Icon(
            imageVector = Icons.Rounded.Favorite,
            contentDescription = "Heart Icon",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(sizeInfinite.value),
            tint = colorInfinite.value
        )
    }
}



























