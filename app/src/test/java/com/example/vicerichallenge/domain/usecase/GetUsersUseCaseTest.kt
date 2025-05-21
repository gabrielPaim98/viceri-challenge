package com.example.vicerichallenge.domain.usecase

import app.cash.turbine.test
import com.example.vicerichallenge.core.util.PagedResult
import com.example.vicerichallenge.core.util.Resource
import com.example.vicerichallenge.domain.model.User
import com.example.vicerichallenge.domain.repository.UserRepository
import com.example.vicerichallenge.fakes.FakeUser
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetUsersUseCaseTest {

    private lateinit var repository: UserRepository
    private lateinit var useCase: GetUsersUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        useCase = GetUsersUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit Loading then Success`() = runTest {
        val users = FakeUser.list
        val pagedResult = PagedResult(items = users, totalItems = 1)

        coEvery {
            repository.getUsers(forceRefresh = false, currentPage = 1, pageSize = 5)
        } returns flow {
            emit(Resource.Loading())
            emit(Resource.Success(pagedResult))
        }

        useCase(forceRefresh = false, currentPage = 1, pageSize = 5).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertEquals(Resource.Success(pagedResult), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `should emit Error when repository fails`() = runTest {
        val errorMessage = "Erro ao buscar usuários"

        coEvery {
            repository.getUsers(forceRefresh = false, currentPage = 1, pageSize = 5)
        } returns flow {
            emit(Resource.Error(errorMessage))
        }

        useCase(forceRefresh = false, currentPage = 1, pageSize = 5).test {
            assertEquals(Resource.Error<PagedResult<User>>(errorMessage), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `should call repository with correct parameters`() = runTest {
        val slotForceRefresh = slot<Boolean>()
        val slotPage = slot<Int>()
        val slotSize = slot<Int>()

        coEvery {
            repository.getUsers(
                forceRefresh = capture(slotForceRefresh),
                currentPage = capture(slotPage),
                pageSize = capture(slotSize)
            )
        } returns emptyFlow()

        useCase(forceRefresh = true, currentPage = 3, pageSize = 10).collect {}

        assertEquals(true, slotForceRefresh.captured)
        assertEquals(3, slotPage.captured)
        assertEquals(10, slotSize.captured)
    }
}