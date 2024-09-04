package com.example.financetracker.data.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "paymentSource")
data class PaymentSource(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
)
