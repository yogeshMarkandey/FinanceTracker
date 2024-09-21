package com.example.financetracker.presentation.screens.navigation

class Routes {
    companion object {
        const val EditTransactionScreen = "/editTransactionScreen?transactionId={transactionId}"
        const val EditCategoryScreen = "/editCategoryScreen?categoryId={categoryId}"
        const val ROOT = "/root"


        fun routeEditCategory(categoryId: Int? = null): String {
            return EditCategoryScreen.replace("{categoryId}", "${categoryId ?: -1}")
        }

        fun routeEditTransaction(transactionId: Int?= null) : String {
            return EditTransactionScreen.replace("{transactionId}", "${transactionId ?: -1}")
        }
    }
}