package com.example.financetracker.presentation.di

import android.content.Context
import com.example.financetracker.data.dataprovider.local.TransactionDatabase
import com.example.financetracker.data.models.local.dao.BudgetDAO
import com.example.financetracker.data.models.local.dao.CategoryDAO
import com.example.financetracker.data.models.local.dao.CategoryWiseBudgetDAO
import com.example.financetracker.data.models.local.dao.TransactionBudgetCrossDAO
import com.example.financetracker.data.models.local.dao.TransactionsDAO
import com.example.financetracker.data.repository.BudgetRepositoryImpl
import com.example.financetracker.data.repository.TransactionRepositoryImpl
import com.example.financetracker.data.usecase.budget.GetBudgetBetweenUseCaseImpl
import com.example.financetracker.data.usecase.budget.GetBudgetByIdUseCaseImpl
import com.example.financetracker.data.usecase.budget.GetBudgetByStartDateUseCaseImpl
import com.example.financetracker.data.usecase.budget.GetBudgetForDateUseCaseImpl
import com.example.financetracker.data.usecase.budget.GetBudgetForTransactionImpl
import com.example.financetracker.data.usecase.budget.SaveBudgetUseCaseImpl
import com.example.financetracker.data.usecase.budget.UpdateBudgetUseCaseImpl
import com.example.financetracker.data.usecase.category.AddCategoryUseCaseImpl
import com.example.financetracker.data.usecase.category.GetAllCategoryUseCaseImpl
import com.example.financetracker.data.usecase.category.GetCategoryByIdUseCaseImpl
import com.example.financetracker.data.usecase.category.UpdateCategoryUseCaseImpl
import com.example.financetracker.data.usecase.color.GetStandardColorsUseCaseImpl
import com.example.financetracker.data.usecase.icons.GetAvailableIconsUseCaseImpl
import com.example.financetracker.data.usecase.transaction.AddTransactionUseCaseImp
import com.example.financetracker.data.usecase.transaction.GetAllTransactionBetweenUseCaseImp
import com.example.financetracker.data.usecase.transaction.GetTransactionByIdUseCaseImp
import com.example.financetracker.data.usecase.transaction.UpdateTransactionUseCaseImpl
import com.example.financetracker.domain.repository.BudgetRepository
import com.example.financetracker.domain.repository.TransactionRepository
import com.example.financetracker.domain.usecase.budget.GetBudgetBetweenUseCase
import com.example.financetracker.domain.usecase.budget.GetBudgetByIdUseCase
import com.example.financetracker.domain.usecase.budget.GetBudgetByStartDateUseCase
import com.example.financetracker.domain.usecase.budget.GetBudgetForDateUseCase
import com.example.financetracker.domain.usecase.budget.GetBudgetForTransactionUseCase
import com.example.financetracker.domain.usecase.budget.SaveBudgetUseCase
import com.example.financetracker.domain.usecase.budget.UpdateBudgetUseCase
import com.example.financetracker.domain.usecase.category.AddCategoryUseCase
import com.example.financetracker.domain.usecase.category.GetAllCategoryUseCase
import com.example.financetracker.domain.usecase.category.GetCategoryByIdUseCase
import com.example.financetracker.domain.usecase.category.UpdateCategoryUseCase
import com.example.financetracker.domain.usecase.color.GetStandardColorsUseCase
import com.example.financetracker.domain.usecase.icons.GetAvailableIconsUseCase
import com.example.financetracker.domain.usecase.transaction.AddTransactionUseCase
import com.example.financetracker.domain.usecase.transaction.GetAllTransactionBetweenUseCase
import com.example.financetracker.domain.usecase.transaction.GetTransactionByIdUseCase
import com.example.financetracker.domain.usecase.transaction.UpdateTransactionUseCase
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
    fun getBudgetDao(db: TransactionDatabase): BudgetDAO {
        return db.budgetDao()
    }

    @Singleton
    @Provides
    fun getCategoryBudgetDao(db: TransactionDatabase): CategoryWiseBudgetDAO {
        return db.categoryBudgetDao()
    }

    @Singleton
    @Provides
    fun getTransactionBudgetCrossDao(db: TransactionDatabase): TransactionBudgetCrossDAO {
        return db.transactionBudgetCrossDao()
    }

    @Singleton
    @Provides
    fun getBudgetRepository(
        budgetDAO: BudgetDAO,
        categoryBudgetDAO: CategoryWiseBudgetDAO,
        categoryDAO: CategoryDAO,
        transactionBudgetCrossDAO: TransactionBudgetCrossDAO,
    ): BudgetRepository {
        return BudgetRepositoryImpl(
            budgetDAO = budgetDAO,
            categoryBudgetDAO = categoryBudgetDAO,
            categoryDAO = categoryDAO,
            transactionCrossRef = transactionBudgetCrossDAO,
        )
    }

    @Singleton
    @Provides
    fun getTransactionRepository(
        categoryDAO: CategoryDAO,
        transactionsDAO: TransactionsDAO,
        crossDAO: TransactionBudgetCrossDAO,
    ): TransactionRepository {
        return TransactionRepositoryImpl(
            categoryDAO = categoryDAO,
            transactionsDAO = transactionsDAO,
            transactionBudgetCrossDAO = crossDAO,
        )
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
    fun getGetCategoryByIdUseCase(repository: TransactionRepository): GetCategoryByIdUseCase {
        return GetCategoryByIdUseCaseImpl(repository)
    }

    @Provides
    fun getUpdateCategoryUseCase(transactionRepository: TransactionRepository): UpdateCategoryUseCase {
        return UpdateCategoryUseCaseImpl(transactionRepository)
    }

    @Provides
    fun getGetStandardColorUseCase(@ApplicationContext context: Context): GetStandardColorsUseCase {
        return GetStandardColorsUseCaseImpl(context)
    }

    @Provides
    fun getGetAvailableIconsUseCase(): GetAvailableIconsUseCase {
        return GetAvailableIconsUseCaseImpl()
    }

    @Provides
    fun getAllTransactionUC(repository: TransactionRepository): GetAllTransactionBetweenUseCase {
        return GetAllTransactionBetweenUseCaseImp(repository)
    }

    @Provides
    fun getUpdateTransactionUC(repository: TransactionRepository): UpdateTransactionUseCase {
        return UpdateTransactionUseCaseImpl(repository)
    }

    @Provides
    fun getAddTransactionUseCase(repository: TransactionRepository): AddTransactionUseCase {
        return AddTransactionUseCaseImp(repository)
    }

    @Provides
    fun getGetTransactionByIdUC(repository: TransactionRepository): GetTransactionByIdUseCase {
        return GetTransactionByIdUseCaseImp(repository)
    }

    @Provides
    fun getSaveBudgetUC(repository: BudgetRepository): SaveBudgetUseCase {
        return SaveBudgetUseCaseImpl(repository)
    }

    @Provides
    fun getGetBudgetByStartDateUC(repository: BudgetRepository): GetBudgetByStartDateUseCase {
        return GetBudgetByStartDateUseCaseImpl(repository)
    }

    @Provides
    fun getGetBudgetByIdUC(repository: BudgetRepository): GetBudgetByIdUseCase {
        return GetBudgetByIdUseCaseImpl(repository)
    }

    @Provides
    fun getUpdateBudgetUC(repository: BudgetRepository): UpdateBudgetUseCase {
        return UpdateBudgetUseCaseImpl(repository)
    }

    @Provides
    fun getBudgetForDateUC(repository: BudgetRepository): GetBudgetForDateUseCase {
        return GetBudgetForDateUseCaseImpl(repository)
    }

    @Provides
    fun getGetBudgetForTransactionUC(repository: BudgetRepository): GetBudgetForTransactionUseCase {
        return GetBudgetForTransactionImpl(repository)
    }

    @Provides
    fun getGetBudgetBetweenUC(repository: BudgetRepository): GetBudgetBetweenUseCase {
        return GetBudgetBetweenUseCaseImpl(repository)
    }
}