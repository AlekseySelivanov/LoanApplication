package com.alexs.local.db.di

import android.content.Context
import androidx.room.Room
import com.alexs.local.db.LoansDatabase
import com.alexs.local.db.dao.LoanDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): LoansDatabase = Room
        .databaseBuilder(
            context,
            LoansDatabase::class.java,
            "Loans database"
        )
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideLoansDao(loansDatabase: LoansDatabase): LoanDao = loansDatabase.loanDao()

}