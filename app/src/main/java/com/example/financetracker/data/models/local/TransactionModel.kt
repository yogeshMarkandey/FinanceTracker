package com.example.financetracker.data.models.local

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row

data class TransactionModel(
    val date: String,
    val valueDate: String,
    val description: String,
    val refNo: String,
    val amount: Double,
    val transactionType: TransactionType,
    val balance: Double
) {
    companion object {
        fun create(row: Row): TransactionModel {
            val date = getDateString(row.getCell(0))
            val valueDate = getDateString(row.getCell(1))
            val description = getStringValue(row.getCell(2))
            val refNo = getStringValue(row.getCell(3))
            val debit: Double = getAmount(row.getCell(4))
            val credit = getAmount(row.getCell(5))
            val txnType =
                if (debit > 0.0) TransactionType.Debit
                else TransactionType.Credit
            val amount = if (txnType == TransactionType.Debit) debit else credit
            val balance = getAmount(row.getCell(6))
            return TransactionModel(
                date = date,
                valueDate = valueDate,
                description = description,
                refNo = refNo,
                amount = amount,
                transactionType = txnType,
                balance = balance
            )
        }

        private fun getAmount(cell: Cell?): Double {
            return try {
                cell?.numericCellValue ?: 0.0
            } catch (e: IllegalStateException) {
                val str = cell?.stringCellValue ?: return 0.0
                val value = try {
                    val rawString = str.replace(",", "")
                    rawString.toDouble()
                } catch (e: Exception) {
                    0.0
                }
                value
            } catch (e: Exception) {
                e.printStackTrace()
                0.0
            }
        }

        private fun getStringValue(cell: Cell?): String {
            return try {
                cell?.stringCellValue ?: "NA"
            } catch (e: Exception) {
                "NA"
            }
        }

        private fun getDateString(cell: Cell?): String {
            return try {
                cell?.dateCellValue?.toString() ?: ""
            } catch (e: Exception) {
                ""
            }
        }
    }
}

enum class TransactionType {
    Credit,
    Debit
}