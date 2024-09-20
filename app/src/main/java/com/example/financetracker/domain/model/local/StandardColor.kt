package com.example.financetracker.domain.model.local

import androidx.compose.ui.graphics.Color
import com.example.financetracker.presentation.utils.fromHex

data class StandardColor(
    val hex: String,
    val name: String
) {
    companion object {
        fun red(): StandardColor {
            return StandardColor("#FF0000", "Red")
        }

        fun StandardColor.toColor(): Color {
            return Color.fromHex(hex)
        }
    }
}
