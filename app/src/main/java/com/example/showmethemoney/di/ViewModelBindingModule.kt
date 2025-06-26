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

@Module
abstract class ViewModelBindingModule {
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