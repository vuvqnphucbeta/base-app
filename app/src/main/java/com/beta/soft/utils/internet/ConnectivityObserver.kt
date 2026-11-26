package com.beta.soft.utils.internet

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    enum class Status {
        Available, Losing, Lost, Unavailable
    }

    fun observe(): Flow<Status>
    suspend fun checkInternetAccess(): Status
}