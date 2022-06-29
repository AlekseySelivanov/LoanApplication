package com.alexs.common

object Routes {

    private const val DOMAIN = "loanapp://navigation"

    const val AUTHORIZATION = "$DOMAIN/authorization"
    const val HOME = "$DOMAIN/home"

    fun loanRoute(loanId: Int): String {
        return "$DOMAIN/loan?loanId=$loanId"
    }

}