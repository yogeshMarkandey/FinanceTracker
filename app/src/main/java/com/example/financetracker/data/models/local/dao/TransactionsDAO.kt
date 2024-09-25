package com.example.financetracker.data.models.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.financetracker.data.models.TransactionEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TransactionsDAO {
    @Query("SELECT * FROM transactions")
    fun getAllTransaction(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE dateTime BETWEEN :start AND :end")
    fun getBetween(start: Date, end: Date) : Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id IN (:transactionIds)")
    fun loadAllByIds(transactionIds: IntArray): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    fun getById(id: Int) : TransactionEntity?

    @Insert
    fun insertAll(vararg transactions: TransactionEntity)

    @Delete
    fun delete(transaction: TransactionEntity)

    @Update
    fun update(transaction: TransactionEntity)
}