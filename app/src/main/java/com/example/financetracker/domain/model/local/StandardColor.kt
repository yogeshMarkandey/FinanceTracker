package com.example.financetracker.domain.model.local

data class StandardColor(
    val hex: String,
    val name: String
) {
    companion object {
        fun red(): StandardColor {
            return StandardColor("#FF0000", "Red")
        }
    }
}
