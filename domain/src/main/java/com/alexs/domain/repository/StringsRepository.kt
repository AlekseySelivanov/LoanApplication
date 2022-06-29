package com.alexs.domain.repository

interface StringsRepository {

    val blankAmountMessage: String
    val inNumericAmountMessage: String
    val zeroAmountMessage: String
    val highAmountMessage: String

    val blankEmailMessage: String
    val invalidEmailMessage: String

    val blankFullNameMessage: String
    val shortFullNameMessage: String

    val blankPasswordMessage: String
    val shortPasswordMessage: String
    val incorrectPasswordFormat: String
    val notMatchingPassword: String

    val blankPhoneMessage: String
    val invalidPhone: String

}