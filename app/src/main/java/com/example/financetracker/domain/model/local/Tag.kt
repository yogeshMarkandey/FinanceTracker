package com.example.financetracker.domain.model.local

import com.example.financetracker.data.models.local.PaymentType
import com.example.financetracker.data.models.local.TagEntity
import java.util.Date

data class Tag(
    val id: Int,
    val title: String,
    val createdOn: Date,
    val updatedOn: Date,
    val notes: String,
    val paymentType: PaymentType,
    val icon: String,
    val color: String
) {
    companion object {
        fun TagEntity.toTag(): Tag {
            return Tag(
                id = id,
                title = title,
                createdOn = createdOn,
                updatedOn = updatedOn,
                notes = notes,
                paymentType = paymentType,
                icon = icon,
                color = color
            )
        }

        fun Tag.toTagEntity(): TagEntity {
            return TagEntity(
                id = id,
                title = title,
                createdOn = createdOn,
                updatedOn = updatedOn,
                notes = notes,
                paymentType = paymentType,
                icon = icon,
                color = color
            )
        }
    }
}
