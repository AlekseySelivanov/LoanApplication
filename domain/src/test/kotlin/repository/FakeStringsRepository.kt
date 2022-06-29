package repository

import com.alexs.domain.repository.StringsRepository

class FakeStringsRepository : StringsRepository {
    override val blankAmountMessage: String
        get() = "blankAmountMessage"
    override val inNumericAmountMessage: String
        get() = "inNumericAmountMessage"
    override val zeroAmountMessage: String
        get() = "zeroAmountMessage"
    override val highAmountMessage: String
        get() = "highAmountMessage"
    override val blankEmailMessage: String
        get() = "blankEmailMessage"
    override val invalidEmailMessage: String
        get() = "invalidEmailMessage"
    override val blankFullNameMessage: String
        get() = "blankFullNameMessage"
    override val shortFullNameMessage: String
        get() = "shortFullNameMessage"
    override val blankPasswordMessage: String
        get() = "blankPasswordMessage"
    override val shortPasswordMessage: String
        get() = "shortPasswordMessage"
    override val incorrectPasswordFormat: String
        get() = "incorrectPasswordFormat"
    override val notMatchingPassword: String
        get() = "notMatchingPassword"
    override val blankPhoneMessage: String
        get() = "blankPhoneMessage"
    override val invalidPhone: String
        get() = "invalidPhone"
}