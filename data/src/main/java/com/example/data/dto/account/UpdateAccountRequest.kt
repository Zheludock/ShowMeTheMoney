package com.example.data.dto.account

data class UpdateAccountRequest(
    val name: String?,
    val balance: String?,
    val currency: String?
)