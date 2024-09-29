package jp.speakbuddy.edisonandroidexercise.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import jp.speakbuddy.edisonandroidexercise.ui.fact.FactViewModel

@Module
@InstallIn(SingletonComponent::class)
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FactViewModel::class)
    abstract fun bindFactViewModel(viewModel: FactViewModel): ViewModel
}