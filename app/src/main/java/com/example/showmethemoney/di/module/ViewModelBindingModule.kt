package com.example.showmethemoney.di.module

import androidx.lifecycle.ViewModel
import com.example.account.AccountViewModel
import com.example.account.editaccount.EditAccountViewModel
import com.example.addexpense.AddExpenseViewModel
import com.example.addincome.AddIncomeViewModel
import com.example.analysis.ExpenseAnalysisViewModel
import com.example.analysis.IncomeAnalysisViewModel
import com.example.showmethemoney.ui.screens.NetworkAwareViewModel
import com.example.category.CategoryViewModel
import com.example.editexpence.EditExpenseViewModel
import com.example.editincome.EditIncomeViewModel
import com.example.expenses.ExpensesViewModel
import com.example.expenseshistory.ExpensesHistoryViewModel
import com.example.incomes.IncomesViewModel
import com.example.incomeshistory.IncomesHistoryViewModel
import com.example.showmethemoney.di.util.ViewModelKey
import com.example.showmethemoney.di.scopes.ActivityScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Dagger модуль для привязки ViewModel к мультибиндингу.
 *
 * Позволяет централизованно регистрировать ViewModel для их последующего создания
 * через [com.example.showmethemoney.di.util.DaggerViewModelFactory]. Каждая ViewModel привязывается к карте с помощью
 * аннотации [@IntoMap] и собственного [@ViewModelKey].
 *
 * Для добавления новой ViewModel необходимо:
 * 1. Создать новый метод с аннотацией @Binds, @IntoMap и @ViewModelKey
 * 2. Указать конкретный класс ViewModel как параметр
 * 3. Вернуть базовый тип ViewModel
 */
@Module
abstract class ViewModelBindingModule {
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
    @ViewModelKey(EditAccountViewModel::class)
    abstract fun bindEditAccountViewModel(viewModel: EditAccountViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(IncomesViewModel::class)
    abstract fun bindIncomesViewModel(viewModel: IncomesViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(ExpensesViewModel::class)
    abstract fun bindExpensesViewModel(viewModel: ExpensesViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(IncomesHistoryViewModel::class)
    abstract fun bindIncomesHistoryViewModel(viewModel: IncomesHistoryViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(ExpensesHistoryViewModel::class)
    abstract fun bindExpensesHistoryViewModel(viewModel: ExpensesHistoryViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(AddExpenseViewModel::class)
    abstract fun bindAddExpenseViewModel(viewModel: AddExpenseViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(AddIncomeViewModel::class)
    abstract fun bindAddIncomeViewModel(viewModel: AddIncomeViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(EditExpenseViewModel::class)
    abstract fun bindEditExpenseViewModel(viewModel: EditExpenseViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(EditIncomeViewModel::class)
    abstract fun bindEditIncomeViewModel(viewModel: EditIncomeViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(IncomeAnalysisViewModel::class)
    abstract fun bindIncomeAnalysisViewModel(viewModel: IncomeAnalysisViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(ExpenseAnalysisViewModel::class)
    abstract fun bindExpenseAnalysisViewModel(viewModel: ExpenseAnalysisViewModel): ViewModel
}