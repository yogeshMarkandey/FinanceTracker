package com.example.financetracker.domain.usecase.analysis

import com.example.financetracker.domain.model.CategoryAnalysisResult
import java.util.Date

interface GetCategoryAnalysisUseCase {
    suspend fun execute(start: Date, end: Date): List<CategoryAnalysisResult>
}