package com.example.vibramobile.contracts

import com.example.vibramobile.models.Bill

interface IBillRepository {
    suspend fun getPaymentHistory(accessToken: String): List<Bill>
}