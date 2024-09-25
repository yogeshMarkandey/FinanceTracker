package com.example.financetracker.data.dataprovider.local


import androidx.room.TypeConverter
import com.example.financetracker.data.models.local.BudgetType
import com.example.financetracker.data.models.local.PaymentType
import com.example.financetracker.data.utils.fromJson
import com.google.gson.Gson
import java.util.Date

class DatabaseTypeConverters {

    // Date
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }


    // Payment Type
    @TypeConverter
    fun paymentTypeToString(type: PaymentType): String {
        return type.name
    }


    @TypeConverter
    fun stringToPaymentType(string: String): PaymentType {
        val type = PaymentType.entries.find { it.name == string }
        return type ?: PaymentType.Expense
    }

    // Budget Type
    @TypeConverter
    fun budgetTypeToString(type: BudgetType): String {
        return type.name
    }

    @TypeConverter
    fun stringToBudgetType(string: String): BudgetType {
        val type = BudgetType.entries.find { it.name == string }
        return type ?: BudgetType.NA
    }

    // String to Array
    @TypeConverter
    fun fromStringArrayList(value: ArrayList<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringArrayList(value: String): ArrayList<String> {
        return try {
            Gson().fromJson<ArrayList<String>>(value) //using extension function
        } catch (e: Exception) {
            arrayListOf()
        }
    }
}