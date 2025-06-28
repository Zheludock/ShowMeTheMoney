package com.example.showmethemoney.di

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass
/**
 * Аннотация-ключ для мультибиндинга ViewModel в Dagger 2.
 *
 * Используется в сочетании с `@IntoMap` для создания мапы:
 * `Map<Class<out ViewModel>, Provider<ViewModel>>`,
 * которая затем используется в [DaggerViewModelFactory].
 *
 * @property value Класс ViewModel, который служит ключом в мапе.
 *                Должен быть наследником [ViewModel].
 *
 * Пример использования:
 * ```
 * @Binds
 * @IntoMap
 * @ViewModelKey(MyViewModel::class)
 * abstract fun bindMyViewModel(viewModel: MyViewModel): ViewModel
 * ```
 */
@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelKey(val value: KClass<out ViewModel>)