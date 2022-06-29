package com.alexs.loanmaster.di

import com.alexs.domain.repository.LoansRepository
import com.alexs.domain.repository.StringsRepository
import com.alexs.domain.repository.UserAccountRepository
import com.alexs.domain.repository.UserDataStoreRepository
import com.alexs.repository.preferences.UserDataStoreImpl
import com.alexs.repository.repository.LoanRepositoryImpl
import com.alexs.repository.repository.StringsRepositoryImpl
import com.alexs.repository.repository.UserAccountRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BindingModule {

    @Binds
    fun bindAuthRepository(authorizationRepositoryImpl: UserAccountRepositoryImpl): UserAccountRepository

    @Binds
    fun bindDataStoreRepository(userDataStoreImpl: UserDataStoreImpl): UserDataStoreRepository

    @Binds
    fun bindLoanRepository(loanRepositoryImpl: LoanRepositoryImpl): LoansRepository

    @Binds
    fun bindStringRepository(stringsRepositoryImpl: StringsRepositoryImpl): StringsRepository

}