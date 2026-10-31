package com.droidpanel.app.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.droidpanel.app.ui.theme.DpBorder
import com.droidpanel.app.ui.theme.DpSurface
import com.droidpanel.app.ui.theme.DpTextPrimary
import com.droidpanel.app.ui.theme.DpTextSecondary

private val PanelShape = RoundedCornerShape(16.dp)

/** Bordered monochrome panel. Clickable panels get a subtle press scale + ripple. */
@Composable
fun DpPanel(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.98f else 1f, tween(120), label = "panelPress")
    Column(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(PanelShape)
            .background(DpSurface)
            .border(BorderStroke(1.dp, DpBorder), PanelShape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interaction,
                        indication = ripple(),
                        role = Role.Button,
                        onClick = onClick,
                    )
                } else Modifier
            )
            .padding(16.dp),
        content = content,
    )
}

@Composable
fun DpIcon(@DrawableRes id: Int, modifier: Modifier = Modifier, tint: Color = DpTextPrimary) {
    Icon(painter = painterResource(id), contentDescription = null, tint = tint, modifier = modifier.size(24.dp))
}

/** Small uppercase monospace label used for section headers and status chips. */
@Composable
fun DpLabel(text: String, modifier: Modifier = Modifier, color: Color = DpTextSecondary) {
    Text(text = text.uppercase(), style = MaterialTheme.typography.labelSmall, color = color, modifier = modifier)
}

@Composable
fun DpChip(text: String, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(50)
    DpLabel(
        text = text,
        modifier = modifier
            .border(BorderStroke(1.dp, DpBorder), shape)
            .padding(horizontal = 8.dp, vertical = 3.dp),
    )
}
