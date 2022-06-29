package use_cases.validation

import com.alexs.domain.repository.StringsRepository
import com.alexs.domain.use_cases.authorization.validation.ValidatePhoneNumber
import org.junit.Assert.assertTrue
import org.junit.Test
import repository.FakeStringsRepository

class PhoneNumberTests {

    private val fakeStringsRepository: StringsRepository = FakeStringsRepository()

    @Test
    fun `validate phone, empty number - returns error`() {
        val name = ""
        val useCase = ValidatePhoneNumber(fakeStringsRepository)

        assertTrue(useCase(name).errorMessage == fakeStringsRepository.blankPhoneMessage)
    }

    @Test
    fun `validate phone, not containing digits - returns error`() {
        val name = "abcderts"
        val useCase = ValidatePhoneNumber(fakeStringsRepository)

        assertTrue(useCase(name).errorMessage == fakeStringsRepository.blankPhoneMessage)
    }

    @Test
    fun `validate phone, phone with letters - returns true`() {
        val name = "+7(916)-234-41-42"
        val useCase = ValidatePhoneNumber(fakeStringsRepository)

        assertTrue(useCase(name).successful)
    }

    @Test
    fun `validate phone, phone without letters - returns true`() {
        val name = "79168797321"
        val useCase = ValidatePhoneNumber(fakeStringsRepository)

        assertTrue(useCase(name).successful)
    }

}