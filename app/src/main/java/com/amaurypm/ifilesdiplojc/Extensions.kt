package com.amaurypm.ifilesdiplojc

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.amaurypm.ifilesdiplojc.ui.theme.SnackbarGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private data class ColoredVisuals(
    val bg: Color,
    val textColor: Color,
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short
) : SnackbarVisuals

fun SnackbarHostState.sbMessage(
    scope: CoroutineScope,
    text: String,
    bgColor: Color = SnackbarGreen,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    scope.launch {
        showSnackbar(
            ColoredVisuals(
                message = text,
                bg = bgColor,
                textColor = Color.White,
                duration = duration
            )
        )
    }
}

// Host que aplica los colores si el visuals es ColoredVisuals
@Composable
fun ColoredSnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(hostState = hostState) { data ->
        val visuals = data.visuals
        val (container, content) = if (visuals is ColoredVisuals) {
            visuals.bg to visuals.textColor
        } else {
            MaterialTheme.colorScheme.inverseSurface to MaterialTheme.colorScheme.inverseOnSurface
        }
        Snackbar(
            snackbarData = data,
            containerColor = container,
            contentColor = content,
            actionColor = content
        )
    }
}