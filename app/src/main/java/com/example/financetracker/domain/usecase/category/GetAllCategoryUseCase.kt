package com.example.financetracker.domain.usecase.category

import com.example.financetracker.domain.model.local.Category
import kotlinx.coroutines.flow.Flow


interface GetAllCategoryUseCase {
    fun execute(): Flow<List<Category>>
}