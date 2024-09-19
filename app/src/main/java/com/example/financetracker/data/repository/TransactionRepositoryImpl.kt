package com.example.financetracker.data.repository

import com.example.financetracker.data.dataprovider.local.TransactionDatabase
import com.example.financetracker.data.models.local.TransactionModel
import com.example.financetracker.data.utils.CustomException
import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.domain.model.local.Category.Companion.toCategory
import com.example.financetracker.domain.model.local.Category.Companion.toCategoryEntity
import com.example.financetracker.domain.model.local.Transaction
import com.example.financetracker.domain.model.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream
import javax.inject.Inject


class TransactionRepositoryImpl @Inject constructor(
    private val db: TransactionDatabase
) : TransactionRepository {

    override fun getTransactionFromExcelFile(inputStream: InputStream): List<TransactionModel> {

        val list = mutableListOf<TransactionModel>()

        try {

            val pkg: OPCPackage = OPCPackage.open(inputStream)
            val workbook = XSSFWorkbook(pkg)
            val mySheet = workbook.getSheetAt(0)

            for (i in 0 until mySheet.lastRowNum) {
                if (i == 0) continue
                try {
                    val row = mySheet.getRow(i)
                    if (!row.isTransaction()) {
                        continue
                    }
                    val txn = TransactionModel.create(row)
                    list.add(txn)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    private fun Row.isTransaction(): Boolean {
        return try {
            val c = this.getCell(0)?.dateCellValue?.toString() ?: ""
            c.isNotBlank()
        } catch (e: Exception) {
            false
        }
    }

    override fun getAllTransaction(): Flow<List<Transaction>> {
        TODO("Not yet implemented")
    }

    override fun updateTransaction(transaction: Transaction) {
        TODO("Not yet implemented")
    }

    override fun addTransaction(transaction: Transaction) {
        TODO("Not yet implemented")
    }

    override fun getAllCategories(): Flow<List<Category>> {
        return db.categoryDao().getAllCategories().map { list ->
            list.map { entity ->
                entity.toCategory()
            }
        }
    }

    override fun updateCategory(category: Category) {
        db.categoryDao().updateCategory(category.toCategoryEntity())
    }

    override fun addCategory(category: Category) {
        db.categoryDao().addCategory(category.toCategoryEntity())
    }

    override fun getCategoryById(categoryId: Int): Category {
        val category = db.categoryDao().getCategoryById(categoryId)
            ?: throw CustomException(
                errorMessage = "Category Not Found",
            )
        return category.toCategory()
    }
}