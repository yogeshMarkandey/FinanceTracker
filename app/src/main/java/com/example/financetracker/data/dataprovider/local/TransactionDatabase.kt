package com.example.financetracker.data.dataprovider.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.financetracker.data.models.TransactionEntity
import com.example.financetracker.data.models.local.TransactionsDAO

@Database(
    entities = [TransactionEntity::class],
    exportSchema = true,
    version = 1
)
abstract class TransactionDatabase: RoomDatabase() {
    abstract fun transactionDao(): TransactionsDAO

    companion object {
        @Volatile
        private var INSTANCE: TransactionDatabase? = null

        fun getDatabase(context: Context): TransactionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}