package com.example.utils

data class TopBarState(
    val title: String,
    val onActionClick: (() -> Unit)? = null
)