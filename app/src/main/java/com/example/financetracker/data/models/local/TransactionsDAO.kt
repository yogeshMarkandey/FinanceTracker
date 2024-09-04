package com.example.financetracker.data.models.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.financetracker.data.models.TransactionEntity

@Dao
interface TransactionsDAO {
    @Query("SELECT * FROM transactions")
    fun getAllTransaction(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE id IN (:transactionIds)")
    fun loadAllByIds(transactionIds: IntArray): List<TransactionEntity>


    @Insert
    fun insertAll(vararg transactions: TransactionEntity)

    @Delete
    fun delete(transaction: TransactionEntity)
}