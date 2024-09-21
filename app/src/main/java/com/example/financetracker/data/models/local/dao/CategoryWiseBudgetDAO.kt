package com.example.financetracker.data.models.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.financetracker.data.models.local.CategoryWiseBudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryWiseBudgetDAO {
    @Query("SELECT * FROM categoryBudget")
    fun getAllObservable(): Flow<List<CategoryWiseBudgetEntity>>

    @Query("SELECT * FROM categoryBudget")
    fun getAll(): List<CategoryWiseBudgetEntity>

    @Transaction
    @Query("SELECT * FROM categoryBudget WHERE id IN (:id)")
    fun getAllBy(id: List<String>): List<CategoryWiseBudgetEntity>

    @Query("SELECT * FROM categoryBudget WHERE budgetId = :id")
    fun getAllByBudgetId(id: Int): List<CategoryWiseBudgetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(categoryBudgets: List<CategoryWiseBudgetEntity>)

    @Update
    fun update(categoryBudget: CategoryWiseBudgetEntity)
}