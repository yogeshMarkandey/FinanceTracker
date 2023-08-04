package com.example.financetracker.presentation.screens

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.financetracker.R
import com.example.financetracker.presentation.ui.theme.FinanceTrackerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook


class MainActivity : ComponentActivity() {
    private val TAG = this.javaClass.name;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinanceTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Greeting("Android")
                    }
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            readExcelFile()
        }
    }


    private fun readExcelFile(){
        try {
            Log.d(TAG, "readExcelFile: Called 0")
            val myInput = resources.openRawResource(R.raw.excel_xlsx)
            val pkg: OPCPackage = OPCPackage.open(myInput)
            val workbook  = XSSFWorkbook(pkg)
            Log.d(TAG, "readExcelFile: Called 2")

            val mySheet = workbook.getSheetAt(2)

            val rowIterator: Iterator<Row> = mySheet.rowIterator()
            Log.d(TAG, "readExcelFile: Called 3")

            var counter = 0

            mySheet.forEach {
                Log.d(TAG, "readExcelFile: Cell : ${it.getCell(2).stringCellValue}")
                counter += 1
            }


            Log.d(TAG, "readExcelFile: Counter: ${mySheet.lastRowNum} | ${counter}")
        }catch (e: Exception){
            Log.d(TAG, "readExcelFile: ERROR: ${e.message}")
            e.printStackTrace()
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FinanceTrackerTheme {
        Greeting("Android")
    }
}
