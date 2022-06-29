package com.alexs.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.alexs.local.db.entity.LoanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LoanDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertLoan(loanEntity: LoanEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertLoansList(list: List<LoanEntity>)

    @Query(value = "SELECT * FROM loans_table")
    fun getAllLoans(): Flow<List<LoanEntity>>

}