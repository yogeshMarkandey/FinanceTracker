package com.example.financetracker.data.usecase.category

import com.example.financetracker.data.models.local.dao.CategoryDAO
import com.example.financetracker.data.utils.CustomException
import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.domain.model.local.Category.Companion.toCategory
import com.example.financetracker.domain.model.usecase.category.GetCategoryByIdUseCase
import javax.inject.Inject

class GetCategoryByIdUseCaseImpl @Inject constructor(
    private val dao: CategoryDAO
) : GetCategoryByIdUseCase {
    override suspend fun execute(id: Int): Category {
        val category = dao.getCategoryById(id)
            ?: throw CustomException(
                errorMessage = "Category Not Found",
            )
        return category.toCategory()
    }
}