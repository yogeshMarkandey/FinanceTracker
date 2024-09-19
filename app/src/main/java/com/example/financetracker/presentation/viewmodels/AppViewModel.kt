package com.example.financetracker.presentation.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.financetracker.R
import com.example.financetracker.data.models.local.TransactionModel
import com.example.financetracker.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(

) : ViewModel() {

    private val _isLoading = mutableStateOf(false)

    private val _txnState = mutableStateOf(emptyList<TransactionModel>())
    val txnState get() = _txnState
    val isLoading get() = _isLoading


    fun getDataFromFile(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            _txnState.value = readExcelFile(context)
        }
    }

    fun readExcelFile(context: Context): List<TransactionModel> {


        setLoadingState(true)
        val inputStream = context.resources.openRawResource(R.raw.feb_2023_2)

        val repo = TransactionRepository()
        setLoadingState(false)
        return repo.getTransactionFromExcelFile(inputStream)
    }


    fun setLoadingState(bool: Boolean) {
        _isLoading.value = bool
    }
}