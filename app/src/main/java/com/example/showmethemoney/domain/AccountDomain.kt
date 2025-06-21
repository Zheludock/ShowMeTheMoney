package com.example.showmethemoney.domain

data class AccountDomain(
    val id: String,
    val userId: String,
    var name: String,
    var balance : String,
    var currency: String,
    val createdAt: String,
    val updatedAt: String,
)
