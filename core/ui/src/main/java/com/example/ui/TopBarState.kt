package com.example.ui

data class TopBarState(
    val title: String,
    val onActionClick: (() -> Unit)? = null
)