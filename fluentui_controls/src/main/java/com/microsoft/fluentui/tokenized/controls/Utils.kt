package com.microsoft.fluentui.tokenized.controls

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.microsoft.fluentui.theme.token.*
import com.microsoft.fluentui.theme.token.controlTokens.*
import java.security.InvalidParameterException

@Composable
fun getColorByState(
    stateData: StateColor,
    enabled: Boolean,
    selected: Boolean,
    interactionSource: InteractionSource
): Color {
    if (enabled) {
        val isPressed by interactionSource.collectIsPressedAsState()
        if (selected && isPressed)
            return stateData.selectedPressed
        else if (isPressed)
            return stateData.pressed

        val isFocused by interactionSource.collectIsFocusedAsState()
        if (selected && isFocused)
            return stateData.selectedFocused
        else if (isFocused)
            return stateData.focused

        val isHovered by interactionSource.collectIsHoveredAsState()
        if (selected && isHovered)
            return stateData.selectedFocused
        if (isHovered)
            return stateData.focused

        if (selected)
            return stateData.selected

        return stateData.rest
    } else if (selected)
        return stateData.selectedDisabled
    else
        return stateData.disabled
}

@Composable
fun backgroundColor(
    tokens: ControlToken,
    info: ControlInfo,
    enabled: Boolean,
    selected: Boolean,
    interactionSource: InteractionSource
): Color {
    val backgroundColors: StateColor =
        when (tokens) {
            is ButtonTokens -> tokens.backgroundColor(info as ButtonInfo)
            is FABTokens -> tokens.backgroundColor(info as FABInfo)
            is ToggleSwitchTokens -> tokens.trackColor(info as ToggleSwitchInfo)
            is CheckBoxTokens -> tokens.backgroundColor(info as CheckBoxInfo)
            is RadioButtonTokens -> tokens.backgroundColor(info as RadioButtonInfo)
            else -> throw InvalidParameterException()
        }

    return getColorByState(backgroundColors, enabled, selected, interactionSource)
}

@Composable
fun iconColor(
    tokens: ControlToken,
    info: ControlInfo,
    enabled: Boolean,
    selected: Boolean,
    interactionSource: InteractionSource
): Color {
    val iconColors: StateColor =
        when (tokens) {
            is ButtonTokens -> tokens.iconColor(info as ButtonInfo)
            is FABTokens -> tokens.iconColor(info as FABInfo)
            is ToggleSwitchTokens -> tokens.knobColor(info as ToggleSwitchInfo)
            is CheckBoxTokens -> tokens.iconColor(info as CheckBoxInfo)
            is RadioButtonTokens -> tokens.iconColor(info as RadioButtonInfo)
            else -> throw InvalidParameterException()
        }

    return getColorByState(iconColors, enabled, selected, interactionSource)
}

@Composable
fun textColor(
    tokens: ControlToken,
    info: ControlInfo,
    enabled: Boolean,
    selected: Boolean,
    interactionSource: InteractionSource
): Color {
    val textColors: StateColor =
        when (tokens) {
            is ButtonTokens -> tokens.textColor(info as ButtonInfo)
            is FABTokens -> tokens.textColor(info as FABInfo)
            else -> throw InvalidParameterException()
        }

    return getColorByState(textColors, enabled, selected, interactionSource)
}

@Composable
fun borderStroke(
    tokens: ControlToken,
    info: ControlInfo,
    enabled: Boolean,
    selected: Boolean,
    interactionSource: InteractionSource
): List<BorderStroke> {
    val fetchBorderStroke: StateBorderStroke =
        when (tokens) {
            is ButtonTokens -> tokens.borderStroke(info as ButtonInfo)
            is FABTokens -> tokens.borderStroke(info as FABInfo)
            is CheckBoxTokens -> tokens.borderStroke(info as CheckBoxInfo)
            else -> throw InvalidParameterException()
        }

    if (enabled) {
        val isPressed by interactionSource.collectIsPressedAsState()
        if (selected && isPressed)
            return fetchBorderStroke.selectedPressed
        else if (isPressed)
            return fetchBorderStroke.pressed

        val isFocused by interactionSource.collectIsFocusedAsState()
        if (selected && isFocused)
            return fetchBorderStroke.selectedFocused
        else if (isFocused)
            return fetchBorderStroke.focused

        val isHovered by interactionSource.collectIsHoveredAsState()
        if (selected && isHovered)
            return fetchBorderStroke.selectedFocused
        if (isHovered)
            return fetchBorderStroke.focused

        if (selected)
            return fetchBorderStroke.selected

        return fetchBorderStroke.rest
    } else if (selected)
        return fetchBorderStroke.selectedDisabled
    else
        return fetchBorderStroke.disabled
}

@Composable
fun elevation(
    tokens: ControlToken,
    info: ControlInfo,
    enabled: Boolean,
    selected: Boolean,
    interactionSource: InteractionSource
): Dp {
    val elevationState: StateElevation =
        when (tokens) {
            is FABTokens -> tokens.elevation(info as FABInfo)
            is ToggleSwitchTokens -> tokens.elevation(info as ToggleSwitchInfo)
            else -> throw InvalidParameterException()
        }

    if (enabled) {
        val isPressed by interactionSource.collectIsPressedAsState()
        if (selected && isPressed)
            return elevationState.selectedPressed
        else if (isPressed)
            return elevationState.pressed

        val isFocused by interactionSource.collectIsFocusedAsState()
        if (selected && isFocused)
            return elevationState.selectedFocused
        else if (isFocused)
            return elevationState.focused

        val isHovered by interactionSource.collectIsHoveredAsState()
        if (selected && isHovered)
            return elevationState.selectedFocused
        if (isHovered)
            return elevationState.focused

        if (selected)
            return elevationState.selected

        return elevationState.rest
    } else if (selected)
        return elevationState.selectedDisabled
    else
        return elevationState.disabled
}
