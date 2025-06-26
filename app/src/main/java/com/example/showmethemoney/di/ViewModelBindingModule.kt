package com.example.showmethemoney.di

import androidx.lifecycle.ViewModel
import com.example.showmethemoney.network.NetworkAwareViewModel
import com.example.showmethemoney.ui.screens.sections.AccountViewModel
import com.example.showmethemoney.ui.screens.sections.CategoryViewModel
import com.example.showmethemoney.ui.screens.sections.ExpensesViewModel
import com.example.showmethemoney.ui.screens.sections.subsections.AddTransactionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Dagger модуль для привязки ViewModel к мультибиндингу.
 *
 * Позволяет централизованно регистрировать ViewModel для их последующего создания
 * через [DaggerViewModelFactory]. Каждая ViewModel привязывается к карте с помощью
 * аннотации [@IntoMap] и собственного [@ViewModelKey].
 *
 * Для добавления новой ViewModel необходимо:
 * 1. Создать новый метод с аннотацией @Binds, @IntoMap и @ViewModelKey
 * 2. Указать конкретный класс ViewModel как параметр
 * 3. Вернуть базовый тип ViewModel
 */
@Module
abstract class ViewModelBindingModule {
    /**
     * Привязывает [ExpensesViewModel] к мультибиндингу ViewModel.
     * Далее - аналогично.
     */
    @Binds
    @IntoMap
    @ViewModelKey(ExpensesViewModel::class)
    abstract fun bindExpensesViewModel(viewModel: ExpensesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun bindCategoryViewModel(viewModel: CategoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(viewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NetworkAwareViewModel::class)
    abstract fun bindNetworkAwareViewModel(viewModel: NetworkAwareViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddTransactionViewModel::class)
    abstract fun bindAddTransactionViewModel(viewModel: AddTransactionViewModel): ViewModel
}