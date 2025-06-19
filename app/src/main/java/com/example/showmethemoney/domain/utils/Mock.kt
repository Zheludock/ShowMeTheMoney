package com.example.showmethemoney.domain.utils

import com.example.showmethemoney.domain.AccountDomain
import com.example.showmethemoney.domain.CategoryDomain
import com.example.showmethemoney.domain.ExpensesDomain
import com.example.showmethemoney.domain.IncomeDomain

val mockCategoryDomains = listOf(
    CategoryDomain(
        categoryId = "art1",
        categoryName = "Продукты",
        emoji = "🛒",
        isIncome = false
    ),
    CategoryDomain(
        categoryId = "art2",
        categoryName = "Транспорт",
        emoji = "🚕",
        isIncome = false
    ),
    CategoryDomain(
        categoryId = "art3",
        categoryName = "Развлечения",
        emoji = "🎮",
        isIncome = false
    ),
    CategoryDomain(
        categoryId = "art4",
        categoryName = "Зарплата",
        emoji = "🎮",
        isIncome = true
    ),
)

// Моковые счета
val mockAccountDomains = listOf(
    AccountDomain(
        id = "acc1",
        userId = "user1",
        name = "Основной счет",
        balance = "150000",
        currency = "₽",
        createdAt = "",
        updatedAt = ""
    ),
    AccountDomain(
        id = "acc2",
        userId = "user1",
        name = "Долларовый счет",
        balance = "5000",
        currency = "$",
        createdAt = "",
        updatedAt = ""
    )
)

//Моковые доходы
val mockIncomeDomains = listOf(
    IncomeDomain(
        id = 0.toString(),
        accountId = "acc1",
        categoryId = "art4",
        amount = "151515.00",
        transactionDate = "",
        comment = null,
        createdAt = "",
        updatedAt = ""
    ),
    IncomeDomain(
        id = 1.toString(),
        accountId = "acc2",
        categoryId = "art4",
        amount = "151515.00",
        transactionDate = "",
        comment = null,
        createdAt = "",
        updatedAt = ""
    ),
)

// Моковые расходы
val mockExpens = listOf(
    ExpensesDomain(
        id = "exp1",
        accountId = "acc1",
        categoryId = "art1",
        amount = "2500",
        transactionDate = "2023-10-15",
        comment = "Покупки в Пятерочке",
        createdAt = "",
        updatedAt = ""
    ),
    ExpensesDomain(
        id = "exp2",
        accountId = "acc1",
        categoryId = "art2",
        amount = "780",
        transactionDate = "2023-10-15",
        comment = null,
        createdAt = "",
        updatedAt = ""
    ),
    ExpensesDomain(
        id = "exp3",
        accountId = "acc2",
        categoryId = "art3",
        amount = "15",
        transactionDate = "2023-10-14",
        comment = "Игра в Steam",
        createdAt = "",
        updatedAt = ""
    )
)