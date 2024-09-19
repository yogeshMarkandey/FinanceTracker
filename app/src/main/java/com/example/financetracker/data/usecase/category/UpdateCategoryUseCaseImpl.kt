package com.example.financetracker.data.usecase.category

import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.domain.model.repository.TransactionRepository
import com.example.financetracker.domain.model.usecase.category.UpdateCategoryUseCase
import javax.inject.Inject

class UpdateCategoryUseCaseImpl @Inject constructor(
   private val transactionRepository: TransactionRepository
) : UpdateCategoryUseCase {
    override suspend fun execute(category: Category) {
        transactionRepository.updateCategory(category)
    }
}