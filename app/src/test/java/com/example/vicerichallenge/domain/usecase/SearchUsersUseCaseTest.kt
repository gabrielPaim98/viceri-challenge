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
class SearchUsersUseCaseTest {

    private lateinit var repository: UserRepository
    private lateinit var useCase: SearchUsersUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = SearchUsersUseCase(repository)
    }

    @Test
    fun `should emit success when users are found`() = runTest {
        val query = "john"
        val users = FakeUser.list

        coEvery { repository.searchUsers(query) } returns flowOf(Resource.Success(users))

        useCase(query).test {
            assertEquals(Resource.Success(users), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `should emit error when repository returns error`() = runTest {
        val query = "unknown"
        val errorMessage = "Erro ao buscar usuários"

        coEvery { repository.searchUsers(query) } returns flowOf(Resource.Error(errorMessage))

        useCase(query).test {
            assertEquals(Resource.Error<List<User>>(errorMessage), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `should emit loading then success`() = runTest {
        val query = "john"
        val users = FakeUser.list

        coEvery { repository.searchUsers(query) } returns flowOf(
            Resource.Loading(),
            Resource.Success(users)
        )

        useCase(query).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertEquals(Resource.Success(users), awaitItem())
            awaitComplete()
        }
    }
}