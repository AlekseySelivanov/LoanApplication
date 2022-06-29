package com.alexs.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alexs.local.db.dao.LoanDao
import com.alexs.local.db.entity.LoanEntity

@Database(entities = [LoanEntity::class], version = 2, exportSchema = false)
abstract class LoansDatabase : RoomDatabase() {

    abstract fun loanDao(): LoanDao

}