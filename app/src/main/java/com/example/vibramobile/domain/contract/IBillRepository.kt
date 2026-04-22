package com.example.vibramobile.domain.contract

import com.example.vibramobile.domain.model.Bill

interface IBillRepository {
    suspend fun getPaymentHistory(accessToken: String): List<Bill>
}