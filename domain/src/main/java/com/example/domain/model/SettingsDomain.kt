package com.example.domain.model
/**
 * Доменная модель настройки приложения.
 *
 * @property name Уникальное техническое название настройки (используется для идентификации)
 * @property title Человекочитаемое название настройки для отображения в UI
 * @property isSwitch Флаг, указывающий тип настройки:
 *                   - true: переключатель (switch)
 *                   - false: обычная настройка (по умолчанию)
 */
data class SettingsDomain(
    val name: String,
    val title: String,
    val isSwitch: Boolean = false
)
/**
 * Дефолтный список всех настроек приложения, преобразованный из [AppSetting].
 * Порядок элементов соответствует порядку объявления в enum-классе [AppSetting].
 */
val listSettings = AppSetting.entries.map { it.toDomain() }


