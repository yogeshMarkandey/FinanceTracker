package com.example.financetracker.domain.usecase.category

import com.example.financetracker.domain.model.local.Category

interface GetCategoryByIdUseCase {
    fun execute(id: Int) : Category
}