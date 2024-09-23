package com.example.financetracker.data.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "category")
data class CategoryEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val type: PaymentType,
    val notes: String,
    val createdOn: Date,
    val icon: String,
    val color: String,
    val updatedOn: Date,
)