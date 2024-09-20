package com.example.financetracker.domain.model.usecase.icons

import androidx.compose.ui.graphics.vector.ImageVector

interface GetAvailableIconsUseCase {
    fun execute(): List<ImageVector>
}