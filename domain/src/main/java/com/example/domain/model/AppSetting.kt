package com.example.domain.model

/**
 * Перечисление всех доступных настроек приложения с их метаданными.
 * Каждая настройка содержит:
 * - Уникальный ключ (для сохранения/загрузки)
 * - Понятное название (для отображения в UI)
 * - Тип элемента управления (переключатель или нет)
 *
 * Порядок объявления элементов определяет порядок отображения в интерфейсе.
 * Для изменения порядка нужно поменять местами элементы в enum.
 */
enum class AppSetting(
    val key: String,
    val title: String,
    val isSwitch: Boolean = false,
) {
    DARK_THEME(
        key = "darkTheme",
        title = "Тёмная тема",
        isSwitch = true,
    ),
    MAIN_COLOR(
        key = "mainColor",
        title = "Основной цвет",
    ),
    SOUNDS(
        key = "sounds",
        title = "Звуки",
    ),
    HATPICKS(
        key = "hatpicks",
        title = "Хатпики",
    ),
    CODE(
        key = "code",
        title = "Код пароль",
    ),
    SYNCHRONIZATION(
        key = "synchro",
        title = "Синхронизация",
    ),
    LANGUAGE(
        key = "language",
        title = "Язык",
    ),
    ABOUT(
        key = "about",
        title = "О программе",
    );
/**
 * Конвертирует enum-настройку в доменную модель [com.example.domain.model.SettingsDomain].
 *
 * @return Новый объект SettingsDomain с теми же свойствами.
 */
    fun toDomain() = SettingsDomain(
        name = key,
        title = title,
        isSwitch = isSwitch
    )
}