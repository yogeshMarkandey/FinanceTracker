package com.example.financetracker.data.models.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.financetracker.data.models.local.BudgetEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface BudgetDAO {
    @Query("SELECT * FROM budget")
    fun getAll(): List<BudgetEntity>

    @Query("SELECT * FROM budget")
    fun getAllObservable(): List<BudgetEntity>

    @Query("SELECT * FROM budget WHERE startDate BETWEEN :start AND :end")
    fun getStartBetween(start: Date, end: Date): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budget WHERE :date BETWEEN startDate AND endDate")
    fun getBudgetsForDate(date: Date): List<BudgetEntity>

    @Query("SELECT * FROM budget WHERE id = :id LIMIT 1")
    fun getById(id: Int): BudgetEntity?

    @Insert
    fun add(budget: BudgetEntity)

    @Update
    fun update(budget: BudgetEntity)
}