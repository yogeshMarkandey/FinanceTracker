package com.example.financetracker.data.repository

import com.example.financetracker.data.models.local.TransactionModel
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream


class TransactionRepository {
    fun getTransactionFromExcelFile(inputStream: InputStream): List<TransactionModel> {
        val TAG = "TransactionRepository.KT"

        val list = mutableListOf<TransactionModel>()

        try {

            val pkg: OPCPackage = OPCPackage.open(inputStream)
            val workbook = XSSFWorkbook(pkg)
            val mySheet = workbook.getSheetAt(0)

            for (i in 0 until mySheet.lastRowNum) {
                if (i == 0) continue
                try {
                    val row = mySheet.getRow(i)
                    if(!row.isTransaction()){
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

    private fun Row.isTransaction() : Boolean{
        return try {
            val c  = this.getCell(0)?.dateCellValue?.toString() ?: ""
            c.isNotBlank()
        }catch (e: Exception){
            false
        }
    }
}