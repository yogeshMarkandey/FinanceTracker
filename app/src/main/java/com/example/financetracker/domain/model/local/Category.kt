package com.example.financetracker.domain.model.local

import com.example.financetracker.data.models.local.CategoryEntity
import com.example.financetracker.data.models.local.PaymentType
import java.util.Date

data class Category(
    val id: Int,
    val title: String,
    val type: PaymentType,
    val notes: String,
    val createdOn: Date,
    val icon: String,
    val color: String,
    val updatedOn: Date,
) {
    companion object {
        fun CategoryEntity.toCategory(): Category {
            return Category(
                id = this.id,
                title = title,
                type = type,
                notes = notes,
                createdOn = createdOn,
                icon = icon,
                color = color,
                updatedOn = updatedOn
            )
        }

        fun Category.toCategoryEntity(): CategoryEntity {
            return CategoryEntity(
                id = this.id,
                title = title,
                type = type,
                notes = notes,
                createdOn = createdOn,
                icon = icon,
                color = color,
                updatedOn = updatedOn
            )
        }

        fun getDefaults() : List<Category> {
            return arrayListOf(
                Category(
                    id = 1,
                    title = "Shopping",
                    type = PaymentType.Expense,
                    notes = "",
                    createdOn = Date(),
                    icon = "",
                    color = "",
                    updatedOn = Date(),
                ),
                Category(
                    id = 2,
                    title = "Rent",
                    type = PaymentType.Expense,
                    notes = "",
                    createdOn = Date(),
                    icon = "",
                    color = "",
                    updatedOn = Date(),
                ),
                Category(
                    id = 3,
                    title = "Salary",
                    type = PaymentType.Income,
                    notes = "",
                    createdOn = Date(),
                    icon = "",
                    color = "",
                    updatedOn = Date(),
                ),
                Category(
                    id = 4,
                    title = "EMI",
                    type = PaymentType.Expense,
                    notes = "",
                    createdOn = Date(),
                    icon = "",
                    color = "",
                    updatedOn = Date(),
                )
            )
        }
    }
}
