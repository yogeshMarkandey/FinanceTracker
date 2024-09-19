package com.example.financetracker.data.models.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.financetracker.data.models.local.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDAO {
    @Transaction
    @Query("SELECT * FROM category")
    fun getAllCategories(): Flow<List<CategoryEntity>>


    @Insert
    fun addCategory(vararg category: CategoryEntity)

    @Query("SELECT * FROM category WHERE id = :categoryId LIMIT 1")
    fun getCategoryById(categoryId: Int): CategoryEntity?

    @Update
    fun updateCategory(category: CategoryEntity)
}