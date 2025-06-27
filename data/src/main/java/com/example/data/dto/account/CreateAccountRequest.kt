package com.example.data.dto.account
/**
 * DTO (Data Transfer Object) для создания нового банковского счета.
 * Используется при отправке запроса на создание счета на сервер.
 *
 * @property name Название счета (обязательное поле, 2-100 символов)
 * @property currency Валюта счета (обязательное поле, 3-буквенный код ISO)
 */
data class CreateAccountRequest(
    val name: String,
    val currency: String
)