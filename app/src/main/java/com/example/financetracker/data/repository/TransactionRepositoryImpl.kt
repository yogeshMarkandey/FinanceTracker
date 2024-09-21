package com.example.financetracker.data.repository

import com.example.financetracker.data.dataprovider.local.TransactionDatabase
import com.example.financetracker.data.models.local.TransactionModel
import com.example.financetracker.data.utils.CustomException
import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.domain.model.local.Category.Companion.toCategory
import com.example.financetracker.domain.model.local.Category.Companion.toCategoryEntity
import com.example.financetracker.domain.model.local.Transaction
import com.example.financetracker.domain.model.local.Transaction.Companion.toTransaction
import com.example.financetracker.domain.model.local.Transaction.Companion.toTransactionEntity
import com.example.financetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream
import javax.inject.Inject


class TransactionRepositoryImpl @Inject constructor(
    private val db: TransactionDatabase
) : TransactionRepository {

    private val mapCategory = HashMap<Int, Category>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getAllCategories().collect {
                for (i in it) {
                    mapCategory[i.id] = i
                }
            }
        }
    }

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
        return db.transactionDao().getAllTransaction().map { list ->
            list.map {
                it.toTransaction()
            }
        }
    }

    override fun updateTransaction(transaction: Transaction) {
        db.transactionDao().update(transaction.toTransactionEntity())
    }

    override fun addTransaction(transaction: Transaction) {
        db.transactionDao().insertAll(transaction.toTransactionEntity())
    }

    override fun getTransactionById(id: Int): Transaction? {
        return db.transactionDao().getById(id)?.toTransaction()
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
        val category = mapCategory[categoryId]
            ?: throw CustomException(
                errorMessage = "Category Not Found",
            )
        return category
    }
}