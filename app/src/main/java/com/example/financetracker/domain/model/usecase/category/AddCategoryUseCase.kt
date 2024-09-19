package com.example.financetracker.domain.model.usecase.category

import com.example.financetracker.domain.model.local.Category

interface AddCategoryUseCase {
    fun execute(category: Category)
}