package com.example.showmethemoney.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * Фабрика для создания ViewModel с поддержкой dependency injection через Dagger 2.
 *
 * Использует карту `viewModelsMap`, где:
 * - Ключ: класс ViewModel (`Class<out ViewModel>`)
 * - Значение: провайдер для создания экземпляра ViewModel (`Provider<ViewModel>`)
 *
 * @param viewModelsMap Карта зарегистрированных ViewModel, предоставляемая Dagger.
 * @throws IllegalArgumentException если запрашиваемый ViewModel не найден в графе зависимостей.
 */
class DaggerViewModelFactory @Inject constructor(
    private val viewModelsMap: Map<Class<out ViewModel>,
    @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    /**
     * Создает экземпляр ViewModel указанного типа.
     *
     * @param modelClass Класс ViewModel, который требуется создать.
     * @return Созданный экземпляр ViewModel.
     * @throws IllegalArgumentException если ViewModel не зарегистрирован в Dagger.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val provider = viewModelsMap[modelClass]
            ?: throw IllegalArgumentException("ViewModel $modelClass not found in Dagger graph")
        return provider.get() as T
    }
}