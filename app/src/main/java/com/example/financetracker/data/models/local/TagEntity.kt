package com.example.financetracker.data.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val createdOn: Date,
    val updatedOn: Date,
    val notes: String,
    val paymentType: PaymentType,
    val icon: String,
    val color: String
)
