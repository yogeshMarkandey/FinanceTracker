package com.example.financetracker.data.usecase.category

import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.domain.model.repository.TransactionRepository
import com.example.financetracker.domain.model.usecase.category.GetAllCategoryUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategoryUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository
) : GetAllCategoryUseCase {
    override fun execute(): Flow<List<Category>> {
        return transactionRepository.getAllCategories()
    }
}