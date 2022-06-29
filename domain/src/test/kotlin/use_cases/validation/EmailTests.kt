package use_cases.validation

import com.alexs.domain.repository.StringsRepository
import com.alexs.domain.use_cases.authorization.validation.ValidateEmail
import org.junit.Assert.assertTrue
import org.junit.Test
import repository.FakeStringsRepository

class EmailTests {

    private val fakeStringsRepository: StringsRepository = FakeStringsRepository()

    @Test
    fun `email validation, blank email - returns error`() {
        val emailAddress = ""
        val useCase = ValidateEmail(fakeStringsRepository)

        assertTrue(useCase(emailAddress).errorMessage == fakeStringsRepository.blankEmailMessage)
    }

    @Test
    fun `email validation, missing @ symbol - returns error`() {
        val emailAddress = "examplegmail.com"
        val useCase = ValidateEmail(fakeStringsRepository)

        assertTrue(!useCase(emailAddress).successful)
    }

    @Test
    fun `email validation, missing symbols before @ - returns error`() {
        val emailAddress = "@gmail.com"
        val useCase = ValidateEmail(fakeStringsRepository)

        assertTrue(!useCase(emailAddress).successful)
    }

    @Test
    fun `email validation, missing symbols after @ - returns error`() {
        val emailAddress = "example@.com"
        val useCase = ValidateEmail(fakeStringsRepository)

        assertTrue(!useCase(emailAddress).successful)
    }

    @Test
    fun `email validation, missing dot symbol - returns error`() {
        val emailAddress = "example@gmailcom"
        val useCase = ValidateEmail(fakeStringsRepository)

        assertTrue(!useCase(emailAddress).successful)
    }

    @Test
    fun `email validation, missing symbols after dot - returns error`() {
        val emailAddress = "example@gmail."
        val useCase = ValidateEmail(fakeStringsRepository)

        assertTrue(!useCase(emailAddress).successful)
    }

    @Test
    fun `email validation, missing dot and symbols after it - returns error`() {
        val emailAddress = "example@gmail"
        val useCase = ValidateEmail(fakeStringsRepository)

        assertTrue(!useCase(emailAddress).successful)
    }

    @Test
    fun `email validation, missing all before dot - returns error`() {
        val emailAddress = ".com"
        val useCase = ValidateEmail(fakeStringsRepository)

        assertTrue(!useCase(emailAddress).successful)
    }

    @Test
    fun `email validation, correct email format - asserts true`() {
        val emailAddress = "example@gmail.com"
        val useCase = ValidateEmail(fakeStringsRepository)

        println(useCase(emailAddress))
        assertTrue(useCase(emailAddress).successful)
    }

    @Test
    fun `email validation, correct email format, two domain - asserts true`() {
        val emailAddress = "example@gmail.com.in"
        val useCase = ValidateEmail(fakeStringsRepository)

        assertTrue(useCase(emailAddress).successful)
    }

    @Test
    fun `email validation, name with dot - asserts true`() {
        val emailAddress = "example.example@gmail.com"
        val useCase = ValidateEmail(fakeStringsRepository)

        assertTrue(useCase(emailAddress).successful)
    }

    @Test
    fun `email validation, name with underscore - asserts true`() {
        val emailAddress = "example_example@gmail.com"
        val useCase = ValidateEmail(fakeStringsRepository)

        assertTrue(useCase(emailAddress).successful)
    }

    @Test
    fun `email validation, name with line - asserts true`() {
        val emailAddress = "example-example@gmail.com"
        val useCase = ValidateEmail(fakeStringsRepository)

        assertTrue(useCase(emailAddress).successful)
    }

}