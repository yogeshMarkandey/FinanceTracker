package com.example.financetracker.domain.usecase.transaction

import com.example.financetracker.domain.model.local.Transaction

interface GetTransactionByIdUseCase {
    fun execute(id: Int): Transaction?
}