package com.example.financetracker.data.usecase.category

import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.domain.model.repository.TransactionRepository
import com.example.financetracker.domain.model.usecase.category.AddCategoryUseCase
import javax.inject.Inject

class AddCategoryUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository
) : AddCategoryUseCase {
    override fun execute(category: Category) {
        transactionRepository.addCategory(category)
    }
}