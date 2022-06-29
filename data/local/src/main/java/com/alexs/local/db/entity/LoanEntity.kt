package com.alexs.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loans_table")
data class LoanEntity(
    @PrimaryKey
    @ColumnInfo(name = "loanId")
    val loanId: Int,
    @ColumnInfo(name = "loanAmount")
    val loanAmount: Int,
    @ColumnInfo(name = "loanEnd")
    val loanEnd: String,
    @ColumnInfo(name = "state")
    val state: Int
)
