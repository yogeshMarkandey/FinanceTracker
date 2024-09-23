package com.example.financetracker.presentation.screens.navigation

class Routes {
    companion object {
        const val EditTransactionScreen = "/editTransactionScreen?transactionId={transactionId}"
        const val EditCategoryScreen = "/editCategoryScreen?categoryId={categoryId}"
        const val EditBudgetScreen = "/editBudgetScreen?budgetId={budgetId}"
        const val ROOT = "/root"


        fun routeEditCategory(categoryId: Int? = null): String {
            return EditCategoryScreen.replace("{categoryId}", "${categoryId ?: -1}")
        }

        fun routeEditTransaction(transactionId: Int?= null) : String {
            return EditTransactionScreen.replace("{transactionId}", "${transactionId ?: -1}")
        }

        fun routeEditBudget(budget: Int?= null) : String {
            return EditBudgetScreen.replace("{budgetId}", "${budget ?: -1}")
        }
    }
}