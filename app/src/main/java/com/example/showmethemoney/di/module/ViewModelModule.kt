package com.example.showmethemoney.di.module

import androidx.lifecycle.ViewModelProvider
import com.example.showmethemoney.di.util.DaggerViewModelFactory
import com.example.showmethemoney.di.scopes.ActivityScope
import dagger.Binds
import dagger.Module
/**
 * Dagger модуль для привязки фабрики ViewModel.
 *
 * Связывает кастомную реализацию [DaggerViewModelFactory] с интерфейсом [ViewModelProvider.Factory],
 * что позволяет системе внедрения зависимостей Dagger предоставлять фабрику для создания ViewModel.
 *
 * Этот модуль работает в паре с [ViewModelBindingModule], который предоставляет конкретные реализации ViewModel.
 *
 * @see DaggerViewModelFactory реализация фабрики, использующая Dagger multibinding
 * @see ViewModelProvider.Factory стандартный интерфейс фабрики ViewModel в Android
 */
@Module
interface ViewModelModule {
    /**
     * Привязывает [DaggerViewModelFactory] к [ViewModelProvider.Factory].
     *
     * @param factory Реализация фабрики, предоставляемая Dagger
     * @return Интерфейс фабрики для использования в Android компонентах
     */
    @Binds
    @ActivityScope
    fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}