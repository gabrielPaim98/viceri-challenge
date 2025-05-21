package com.example.vicerichallenge.ui.userList

import com.example.vicerichallenge.core.util.PagedResult
import com.example.vicerichallenge.core.util.Resource
import com.example.vicerichallenge.domain.model.User
import com.example.vicerichallenge.domain.usecase.GetUsersUseCase
import com.example.vicerichallenge.domain.usecase.SearchUsersUseCase
import com.example.vicerichallenge.fakes.FakeUser
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalCoroutinesApi
class UserListViewModelTest {

    private lateinit var getUsersUseCase: GetUsersUseCase
    private lateinit var searchUsersUseCase: SearchUsersUseCase
    private lateinit var viewModel: UserListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        getUsersUseCase = mockk()
        searchUsersUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit loading and then users on fetch`() = runTest {
        val users = FakeUser.list
        val flow = flowOf(
            Resource.Loading(),
            Resource.Success(PagedResult(users, totalItems = 1))
        )

        coEvery { getUsersUseCase(any(), any(), any()) } returns flow

        viewModel = UserListViewModel(getUsersUseCase, searchUsersUseCase)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(1, state.users.size)
        assertEquals(2, state.currentPage)
    }

    @Test
    fun `should emit error on fetch failure`() = runTest {
        val flow = flowOf(Resource.Error<PagedResult<User>>("Something went wrong"))
        coEvery { getUsersUseCase(any(), any(), any()) } returns flow

        viewModel = UserListViewModel(getUsersUseCase, searchUsersUseCase)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Something went wrong", state.errorMessage)
        assertTrue(state.users.isEmpty())
    }

    @Test
    fun `should load more users when loadMoreUsers is called`() = runTest {
        val firstPage = listOf(FakeUser.dummyUser(1))
        val secondPage = listOf(FakeUser.dummyUser(2))

        coEvery { getUsersUseCase(false, 1, any()) } returns flowOf(
            Resource.Loading(),
            Resource.Success(PagedResult(firstPage, totalItems = 2))
        )

        coEvery { getUsersUseCase(false, 2, any()) } returns flowOf(
            Resource.Loading(),
            Resource.Success(PagedResult(secondPage, totalItems = 2))
        )

        viewModel = UserListViewModel(getUsersUseCase, searchUsersUseCase)

        advanceUntilIdle()
        viewModel.loadMoreUsers()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(2, state.users.size)
    }

    @Test
    fun `should emit search results when query is valid`() = runTest {
        val query = "John"
        val result = listOf(FakeUser.dummyUser(99))
        val flow = flowOf(Resource.Loading(), Resource.Success(result))

        coEvery { searchUsersUseCase(query) } returns flow
        coEvery { getUsersUseCase(any(), any(), any()) } returns emptyFlow()

        viewModel = UserListViewModel(getUsersUseCase, searchUsersUseCase)
        advanceUntilIdle()

        viewModel.search(query)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(1, state.users.size)
        assertEquals(99, state.users.first().id)
    }

    @Test
    fun `should emit error when search fails`() = runTest {
        val query = "fail"

        coEvery { searchUsersUseCase(query) } returns flowOf(
            Resource.Loading(),
            Resource.Error("Search failed")
        )
        coEvery { getUsersUseCase(any(), any(), any()) } returns emptyFlow()

        viewModel = UserListViewModel(getUsersUseCase, searchUsersUseCase)
        advanceUntilIdle()

        viewModel.search(query)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Search failed", state.errorMessage)
    }

    @Test
    fun `should refresh when query is blank`() = runTest {
        val users = listOf(FakeUser.dummyUser(5))
        coEvery { getUsersUseCase(any(), any(), any()) } returns flowOf(
            Resource.Loading(),
            Resource.Success(PagedResult(users, totalItems = 1))
        )

        coEvery { searchUsersUseCase(any()) } returns emptyFlow()

        viewModel = UserListViewModel(getUsersUseCase, searchUsersUseCase)
        advanceUntilIdle()

        viewModel.search("   ")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(1, state.users.size)
        assertEquals(5, state.users.first().id)
    }
}