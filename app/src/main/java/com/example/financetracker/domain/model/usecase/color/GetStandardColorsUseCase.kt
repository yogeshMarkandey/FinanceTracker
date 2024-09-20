package com.example.financetracker.domain.model.usecase.color

import com.example.financetracker.domain.model.local.StandardColor

interface GetStandardColorsUseCase {
    fun execute(): List<StandardColor>
}