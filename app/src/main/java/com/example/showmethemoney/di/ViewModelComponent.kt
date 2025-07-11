package com.example.showmethemoney.di

import androidx.lifecycle.ViewModelProvider
import com.example.showmethemoney.ui.MainActivity
import dagger.Component

@ActivityScope
@Component(
    dependencies = [RepositoryComponent::class],
    modules = [
        ViewModelModule::class,
        ViewModelBindingModule::class
    ]
)
interface ViewModelComponent {
    @Component.Factory
    interface Factory {
        fun create(repositoryComponent: RepositoryComponent): ViewModelComponent
    }

    fun inject(activity: MainActivity)
    fun viewModelFactory(): ViewModelProvider.Factory
}