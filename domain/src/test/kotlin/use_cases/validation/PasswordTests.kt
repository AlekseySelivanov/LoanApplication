package use_cases.validation

import com.alexs.domain.repository.StringsRepository
import com.alexs.domain.use_cases.authorization.validation.ValidatePassword
import com.alexs.domain.use_cases.authorization.validation.ValidateRepeatedPassword
import org.junit.Assert
import org.junit.Test
import repository.FakeStringsRepository

class PasswordTests {

    private val fakeStringsRepository: StringsRepository = FakeStringsRepository()

    @Test
    fun `password validation, empty password - asserts true`() {
        val password = ""
        val useCase = ValidatePassword(fakeStringsRepository)

        Assert.assertTrue(useCase(password).errorMessage == fakeStringsRepository.blankPasswordMessage)
    }

    @Test
    fun `password validation, length less than 8 - asserts true`() {
        val password = "pass"
        val useCase = ValidatePassword(fakeStringsRepository)

        Assert.assertTrue(useCase(password).errorMessage == fakeStringsRepository.shortPasswordMessage)
    }

    @Test
    fun `password validation, no digits - asserts true`() {
        val password = "easypassword"
        val useCase = ValidatePassword(fakeStringsRepository)

        Assert.assertTrue(useCase(password).errorMessage == fakeStringsRepository.incorrectPasswordFormat)
    }

    @Test
    fun `password validation, no letters - asserts true`() {
        val password = "123346484659504"
        val useCase = ValidatePassword(fakeStringsRepository)

        Assert.assertTrue(useCase(password).errorMessage == fakeStringsRepository.incorrectPasswordFormat)
    }

    @Test
    fun `repeated password validation, not matches - asserts true`() {
        val password = "easypass1234"
        val repeatedPassword = "easypass5678"
        val useCase = ValidateRepeatedPassword(fakeStringsRepository)

        Assert.assertTrue(!useCase(password, repeatedPassword).successful)
    }

    @Test
    fun `repeated password validation, matches - asserts true`() {
        val password = "easypass1234"
        val repeatedPassword = "easypass1234"
        val useCase = ValidateRepeatedPassword(fakeStringsRepository)

        Assert.assertTrue(useCase(password, repeatedPassword).successful)
    }

    @Test
    fun `password validation, correct password - asserts true`() {
        val password = "easypass1234"
        val useCase = ValidatePassword(fakeStringsRepository)

        Assert.assertTrue(useCase(password).successful)
    }

}