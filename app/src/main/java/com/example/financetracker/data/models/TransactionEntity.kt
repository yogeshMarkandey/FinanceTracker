package com.example.financetracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    val amount: Float,
    @PrimaryKey(autoGenerate = true) val id: Int,
)
