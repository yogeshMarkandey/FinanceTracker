package com.example.financetracker.data.repository

import com.example.financetracker.data.models.local.BudgetTransactionCrossRef
import com.example.financetracker.data.models.local.TransactionModel
import com.example.financetracker.data.models.local.dao.CategoryDAO
import com.example.financetracker.data.models.local.dao.TransactionBudgetCrossDAO
import com.example.financetracker.data.models.local.dao.TransactionsDAO
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
import java.util.Date
import javax.inject.Inject


class TransactionRepositoryImpl @Inject constructor(
    private val categoryDAO: CategoryDAO,
    private val transactionsDAO: TransactionsDAO,
    private val transactionBudgetCrossDAO: TransactionBudgetCrossDAO,
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
        return transactionsDAO.getAllTransaction().map { list ->
            list.map {
                it.toTransaction()
            }
        }
    }

    override fun getAllBetween(start: Date, end: Date): Flow<List<Transaction>> {
        return transactionsDAO.getBetween(start, end).map { list ->
            list.map {
                it.toTransaction()
            }
        }
    }

    override fun updateTransaction(transaction: Transaction) {
        saveTransactionBudgetCrossRef(transaction)
        transactionsDAO.update(transaction.toTransactionEntity())
    }

    private fun saveTransactionBudgetCrossRef(transaction: Transaction) {
        if (transaction.budgets.isNullOrEmpty()) {
            return
        }
        val oldRefs = transactionBudgetCrossDAO.getByTransactionId(transaction.id)

        val map = HashMap<String, BudgetTransactionCrossRef>()
        oldRefs.forEach {
            map["${it.transactionId}_${it.budgetId}"] = it.copyWith(status = "inactive")
        }

        transaction.budgets!!.forEach { bud ->
            val ref = BudgetTransactionCrossRef(
                id = "${transaction.id}_${bud.id}",
                transactionId = transaction.id,
                budgetId = bud.id,
                status = "active"
            )
            map["${ref.transactionId}_${ref.budgetId}"] = ref
        }

        map.values.forEach {
            transactionBudgetCrossDAO.add(it)
        }
    }

    override fun addTransaction(transaction: Transaction) {
        saveTransactionBudgetCrossRef(transaction)
        transactionsDAO.insertAll(transaction.toTransactionEntity())
    }

    override fun getTransactionById(id: Int): Transaction? {
        return transactionsDAO.getById(id)?.toTransaction()
    }

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDAO.getAllCategoriesObservable().map { list ->
            list.map { entity ->
                entity.toCategory()
            }
        }
    }

    override fun updateCategory(category: Category) {
        categoryDAO.updateCategory(category.toCategoryEntity())
    }

    override fun addCategory(category: Category) {
        categoryDAO.addCategory(category.toCategoryEntity())
    }

    override fun getCategoryById(categoryId: Int): Category {
        val category = mapCategory[categoryId]
            ?: throw CustomException(
                errorMessage = "Category Not Found",
            )
        return category
    }
}