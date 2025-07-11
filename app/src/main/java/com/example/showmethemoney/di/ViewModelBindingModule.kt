package com.example.showmethemoney.di

import androidx.lifecycle.ViewModel
import com.example.account.AccountViewModel
import com.example.account.editaccount.EditAccountViewModel
import com.example.showmethemoney.ui.screens.NetworkAwareViewModel
import com.example.category.CategoryViewModel
import com.example.transactions.TransactionViewModel
import com.example.transactions.addtransaction.AddTransactionViewModel
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
     * Привязывает [TransactionViewModel] к мультибиндингу ViewModel.
     * Далее - аналогично.
     */
    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(TransactionViewModel::class)
    abstract fun bindExpensesViewModel(viewModel: TransactionViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun bindCategoryViewModel(viewModel: CategoryViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(viewModel: AccountViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(NetworkAwareViewModel::class)
    abstract fun bindNetworkAwareViewModel(viewModel: NetworkAwareViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(AddTransactionViewModel::class)
    abstract fun bindAddTransactionViewModel(viewModel: AddTransactionViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(EditAccountViewModel::class)
    abstract fun bindEditAccountViewModel(viewModel: EditAccountViewModel): ViewModel
}