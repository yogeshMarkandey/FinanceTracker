package com.example.financetracker.domain.model.local

import android.util.Log
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
            try {
                if (hex.isEmpty()) {
                    return Color.Red
                }
                val color = Color.fromHex(hex)
                return color
            } catch (e: Exception) {
                Log.e("TAG", "toColor: Error Parsing Hex: $hex", e)
                return Color.Yellow
            }
        }
    }
}
