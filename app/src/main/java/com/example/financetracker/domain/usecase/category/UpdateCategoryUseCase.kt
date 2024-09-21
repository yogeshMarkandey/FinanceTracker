package com.example.financetracker.domain.usecase.category

import com.example.financetracker.domain.model.local.Category

interface UpdateCategoryUseCase  {
    suspend fun execute(category: Category)
}