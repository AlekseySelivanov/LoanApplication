package use_cases.validation

import com.alexs.domain.repository.StringsRepository
import com.alexs.domain.use_cases.authorization.validation.ValidateAmount
import org.junit.Assert.assertTrue
import org.junit.Test
import repository.FakeStringsRepository

class AmountTests {

    private val fakeStringsRepository: StringsRepository = FakeStringsRepository()

    @Test
    fun `amount validation, empty amount - returns error`() {
        val amount = ""
        val useCase = ValidateAmount(fakeStringsRepository)

        assertTrue(useCase(amount, "500").errorMessage == fakeStringsRepository.blankAmountMessage)
    }

    @Test
    fun `amount validation, incorrect letters - returns error`() {
        val amount = "123dt"
        val useCase = ValidateAmount(fakeStringsRepository)

        assertTrue(
            useCase(
                amount, maxAmount = "500"
            ).errorMessage == fakeStringsRepository.inNumericAmountMessage
        )
    }

    @Test
    fun `amount validation, amount less than zero - returns error`() {
        val amount = "-20"
        val useCase = ValidateAmount(fakeStringsRepository)

        assertTrue(
            useCase(
                amount,
                maxAmount = "500"
            ).errorMessage == fakeStringsRepository.zeroAmountMessage
        )
    }

    @Test
    fun `amount validation, higher than Integer - returns error`() {
        val amount = "2222222222"
        val useCase = ValidateAmount(fakeStringsRepository)

        assertTrue(
            useCase(
                amount,
                maxAmount = "100000"
            ).errorMessage == fakeStringsRepository.highAmountMessage
        )
    }

    @Test
    fun `amount validation, amount higher than max value - returns error`() {
        val amount = "15000"
        val useCase = ValidateAmount(fakeStringsRepository)

        assertTrue(
            useCase(
                amount,
                maxAmount = "500"
            ).errorMessage == fakeStringsRepository.highAmountMessage
        )
    }

    @Test
    fun `amount validation, correct input - returns true`() {
        val amount = "15000"
        val useCase = ValidateAmount(fakeStringsRepository)

        assertTrue(useCase(amount, maxAmount = "20000").successful)
    }

}