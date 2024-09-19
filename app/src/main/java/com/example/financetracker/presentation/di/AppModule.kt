package com.example.financetracker.presentation.di

import android.content.Context
import com.example.financetracker.data.dataprovider.local.TransactionDatabase
import com.example.financetracker.data.models.local.dao.CategoryDAO
import com.example.financetracker.data.models.local.dao.TransactionsDAO
import com.example.financetracker.data.repository.TransactionRepositoryImpl
import com.example.financetracker.data.usecase.category.AddCategoryUseCaseImpl
import com.example.financetracker.data.usecase.category.GetAllCategoryUseCaseImpl
import com.example.financetracker.data.usecase.category.GetCategoryByIdUseCaseImpl
import com.example.financetracker.data.usecase.category.UpdateCategoryUseCaseImpl
import com.example.financetracker.domain.model.repository.TransactionRepository
import com.example.financetracker.domain.model.usecase.category.AddCategoryUseCase
import com.example.financetracker.domain.model.usecase.category.GetAllCategoryUseCase
import com.example.financetracker.domain.model.usecase.category.GetCategoryByIdUseCase
import com.example.financetracker.domain.model.usecase.category.UpdateCategoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getDatabase(@ApplicationContext context: Context): TransactionDatabase {
        return TransactionDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun getTransactionDao(db: TransactionDatabase): TransactionsDAO {
        return db.transactionDao()
    }

    @Singleton
    @Provides
    fun getCategoryDao(db: TransactionDatabase): CategoryDAO {
        return db.categoryDao()
    }

    @Singleton
    @Provides
    fun getTransactionRepository(db: TransactionDatabase): TransactionRepository {
        return TransactionRepositoryImpl(db = db)
    }

    @Provides
    fun getGetAllCategoryUseCase(transactionRepository: TransactionRepository): GetAllCategoryUseCase {
        return GetAllCategoryUseCaseImpl(transactionRepository)
    }

    @Provides
    fun getAddCategoryUseCase(transactionRepository: TransactionRepository): AddCategoryUseCase {
        return AddCategoryUseCaseImpl(transactionRepository)
    }

    @Provides
    fun getGetCategoryByIdUseCase(categoryDAO: CategoryDAO): GetCategoryByIdUseCase {
        return GetCategoryByIdUseCaseImpl(categoryDAO)
    }

    @Provides
    fun getUpdateCategoryUseCase(transactionRepository: TransactionRepository): UpdateCategoryUseCase {
        return UpdateCategoryUseCaseImpl(transactionRepository)
    }
}