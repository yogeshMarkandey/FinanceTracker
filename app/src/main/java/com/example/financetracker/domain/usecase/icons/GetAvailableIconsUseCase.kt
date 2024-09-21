package com.example.financetracker.domain.usecase.icons

import androidx.compose.ui.graphics.vector.ImageVector

interface GetAvailableIconsUseCase {
    fun execute(): List<ImageVector>
}