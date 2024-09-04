package com.example.financetracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.financetracker.data.models.local.PaymentType
import java.util.Date

@Entity(tableName = "transactions")
data class TransactionEntity(
    val amount: Float,
    @PrimaryKey(autoGenerate = true) val id: Int,
    val dateTime: Date,
    val categoryId: Int,
    val tagId: Int,
    val notes: String,
    val paymentType: PaymentType,
    val paymentSourceId: Int
)
