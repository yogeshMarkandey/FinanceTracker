package com.example.financetracker.data.models.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.financetracker.data.models.local.BudgetTransactionCrossRef

@Dao
interface TransactionBudgetCrossDAO {
    @Query("SELECT * FROM budgettransactioncrossref WHERE budgetId = :budgetId AND status = :status")
    fun getByBudgetId(budgetId: Int, status: String = "active"): List<BudgetTransactionCrossRef>

    @Query("SELECT * FROM budgettransactioncrossref WHERE transactionId = :id AND status = :status")
    fun getByTransactionId(id: Int, status: String = "active"): List<BudgetTransactionCrossRef>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(entry: BudgetTransactionCrossRef)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(entry: BudgetTransactionCrossRef)
}