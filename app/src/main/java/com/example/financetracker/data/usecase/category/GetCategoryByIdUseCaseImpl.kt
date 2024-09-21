package com.example.financetracker.data.usecase.category

import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.domain.repository.TransactionRepository
import com.example.financetracker.domain.usecase.category.GetCategoryByIdUseCase
import javax.inject.Inject

class GetCategoryByIdUseCaseImpl @Inject constructor(
    private val repository: TransactionRepository
) : GetCategoryByIdUseCase {
    override fun execute(id: Int): Category {
        val category = repository.getCategoryById(id)
        return category
    }
}