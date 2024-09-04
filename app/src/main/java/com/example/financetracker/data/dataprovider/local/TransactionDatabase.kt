package com.example.financetracker.data.dataprovider.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.financetracker.data.models.TransactionEntity
import com.example.financetracker.data.models.local.BudgetEntity
import com.example.financetracker.data.models.local.CategoryEntity
import com.example.financetracker.data.models.local.CategoryWiseBudgetEntity
import com.example.financetracker.data.models.local.PaymentSource
import com.example.financetracker.data.models.local.TagEntity
import com.example.financetracker.data.models.local.TransactionsDAO

@Database(
    entities = arrayOf(
        TransactionEntity::class,
        BudgetEntity::class,
        CategoryEntity::class,
        PaymentSource::class,
        TagEntity::class,
        CategoryWiseBudgetEntity::class
    ),
    exportSchema = true,
    version = 3,
)
@TypeConverters(DatabaseTypeConverters::class)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionsDAO

    companion object {
        @Volatile
        private var INSTANCE: TransactionDatabase? = null

        fun getDatabase(context: Context): TransactionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDatabase::class.java,
                    name= "transactions-db",
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}