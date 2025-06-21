package com.example.showmethemoney.domain

data class SettingsDomain(
    val name: String,
    val title: String,
    val isSwitch: Boolean = false
)

val listSettins = listOf<SettingsDomain>(
    SettingsDomain("darkTheme", "Тёмная тема", true),
    SettingsDomain("mainColor", "Основной цвет"),
    SettingsDomain("sounds", "Звуки"),
    SettingsDomain("hatpicks", "Хатпики"),
    SettingsDomain("code", "Код пароль"),
    SettingsDomain("synchro", "Синхронизация"),
    SettingsDomain("language", "Язык"),
    SettingsDomain("about", "О программе"),
)
