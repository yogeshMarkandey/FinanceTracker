package com.example.financetracker.domain.model.usecase.category

import com.example.financetracker.domain.model.local.Category

interface GetCategoryByIdUseCase {
    fun execute(id: Int) : Category
}