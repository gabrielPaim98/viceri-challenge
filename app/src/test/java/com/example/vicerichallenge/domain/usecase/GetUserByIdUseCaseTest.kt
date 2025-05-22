package com.example.vicerichallenge.domain.usecase

import app.cash.turbine.test
import com.example.vicerichallenge.core.util.Resource
import com.example.vicerichallenge.domain.model.User
import com.example.vicerichallenge.domain.repository.UserRepository
import com.example.vicerichallenge.fakes.FakeUser
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetUserByIdUseCaseTest {

    private lateinit var repository: UserRepository
    private lateinit var getUserByIdUseCase: GetUserByIdUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getUserByIdUseCase = GetUserByIdUseCase(repository)
    }

    @Test
    fun `should emit success resource when user is found`() = runTest {
        val user = FakeUser.default
        val userId = user.id

        coEvery { repository.getUserById(userId) } returns flowOf(Resource.Success(user))

        getUserByIdUseCase(userId).test {
            val item = awaitItem()
            assertEquals(Resource.Success(user), item)
            awaitComplete()
        }
    }

    @Test
    fun `should emit error resource when user is not found`() = runTest {
        val userId = 999
        val errorMessage = "Usuário não encontrado"

        coEvery { repository.getUserById(userId) } returns flowOf(Resource.Error(errorMessage))

        getUserByIdUseCase(userId).test {
            val item = awaitItem()
            assertEquals(Resource.Error<User>(errorMessage), item)
            awaitComplete()
        }
    }

    @Test
    fun `should emit loading then success`() = runTest {
        val user = FakeUser.default
        val userId = user.id

        coEvery { repository.getUserById(userId) } returns flowOf(
            Resource.Loading(),
            Resource.Success(user)
        )

        getUserByIdUseCase(userId).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertEquals(Resource.Success(user), awaitItem())
            awaitComplete()
        }
    }
}