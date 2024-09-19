package com.example.financetracker.domain.model.usecase.category

import com.example.financetracker.domain.model.local.Category

interface GetCategoryByIdUseCase {
    suspend fun execute(id: Int) : Category
}