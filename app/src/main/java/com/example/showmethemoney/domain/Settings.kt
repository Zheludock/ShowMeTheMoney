package com.example.showmethemoney.domain

data class Settings(
    val name: String,
    val title: String,
    val isSwitch: Boolean = false
)

val listSettins = listOf<Settings>(
    Settings("darkTheme", "Тёмная тема", true),
    Settings("mainColor", "Основной цвет"),
    Settings("sounds", "Звуки"),
    Settings("hatpicks", "Хатпики"),
    Settings("code", "Код пароль"),
    Settings("synchro", "Синхронизация"),
    Settings("language", "Язык"),
    Settings("about", "О программе"),
)
