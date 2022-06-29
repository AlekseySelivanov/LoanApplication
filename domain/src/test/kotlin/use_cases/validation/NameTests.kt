package use_cases.validation

import com.alexs.domain.repository.StringsRepository
import com.alexs.domain.use_cases.authorization.validation.ValidateFullName
import com.alexs.domain.use_cases.authorization.validation.ValidateName
import org.junit.Assert.assertTrue
import org.junit.Test
import repository.FakeStringsRepository

class NameTests {

    private val fakeStringsRepository: StringsRepository = FakeStringsRepository()

    @Test
    fun `validate full name, name is blank - returns error`() {
        val fullName = ""
        val useCase = ValidateFullName(fakeStringsRepository)

        assertTrue(useCase(fullName).errorMessage == fakeStringsRepository.blankFullNameMessage)
    }

    @Test
    fun `validate full name, name is short - returns error`() {
        val fullName = ""
        val useCase = ValidateFullName(fakeStringsRepository)

        assertTrue(useCase(fullName).errorMessage == fakeStringsRepository.blankFullNameMessage)
    }

    @Test
    fun `validate full name, name contains 1 word - returns error`() {
        val fullName = "Test"
        val useCase = ValidateFullName(fakeStringsRepository)

        assertTrue(useCase(fullName).errorMessage == fakeStringsRepository.shortFullNameMessage)
    }

    @Test
    fun `validate full name, name contains two words - returns true`() {
        val fullName = "Test Test"
        val useCase = ValidateFullName(fakeStringsRepository)

        assertTrue(useCase(fullName).successful)
    }

    @Test
    fun `validate name, empty name - returns error`() {
        val name = ""
        val useCase = ValidateName(fakeStringsRepository)

        assertTrue(useCase(name).errorMessage == fakeStringsRepository.blankFullNameMessage)
    }

    @Test
    fun `validate name, correct name - returns true`() {
        val name = "Name"
        val useCase = ValidateName(fakeStringsRepository)

        assertTrue(useCase(name).successful)
    }

    @Test
    fun `validate full name, name contains three words - returns true`() {
        val fullName = "Test Test"
        val useCase = ValidateFullName(fakeStringsRepository)

        assertTrue(useCase(fullName).successful)
    }

}