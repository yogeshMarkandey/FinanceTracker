package com.example.financetracker.domain.model.local

import com.example.financetracker.data.models.local.PaymentSourceEntity

data class PaymentSource(
    val id: Int,
    val title: String,
) {
    companion object {
        fun PaymentSourceEntity.toPaymentSource(): PaymentSource {
            return PaymentSource(id, title)
        }

        fun PaymentSource.toEntity(): PaymentSourceEntity {
            return PaymentSourceEntity(id, title)
        }
    }
}
