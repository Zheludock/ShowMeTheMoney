package com.example.showmethemoney.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountManager @Inject constructor() {
    var selectedAccountId: Int = -1
}