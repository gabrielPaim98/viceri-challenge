package com.example.vicerichallenge.ui.userDetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.vicerichallenge.core.util.Resource
import com.example.vicerichallenge.domain.usecase.GetUserByIdUseCase
import com.example.vicerichallenge.fakes.FakeUser
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserDetailsViewModelTest {

    private lateinit var viewModel: UserDetailsViewModel
    private lateinit var getUserByIdUseCase: GetUserByIdUseCase
    private lateinit var savedStateHandle: SavedStateHandle

    private val user = FakeUser.default

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        getUserByIdUseCase = mockk()
        savedStateHandle = SavedStateHandle(mapOf("userId" to 1))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit Loading then Success when use case returns user`() = runTest {
        coEvery { getUserByIdUseCase(1) } returns flow {
            emit(Resource.Loading())
            emit(Resource.Success(user))
        }

        viewModel = UserDetailsViewModel(getUserByIdUseCase, savedStateHandle)

        viewModel.state.test {
            assertEquals(UserDetailsState.Loading, awaitItem())
            assertEquals(UserDetailsState.Success(user), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Loading then Error when use case returns error`() = runTest {
        val errorMessage = "Usuário não encontrado"
        coEvery { getUserByIdUseCase(1) } returns flow {
            emit(Resource.Loading())
            emit(Resource.Error(errorMessage))
        }

        viewModel = UserDetailsViewModel(getUserByIdUseCase, savedStateHandle)

        viewModel.state.test {
            assertEquals(UserDetailsState.Loading, awaitItem())
            assertEquals(UserDetailsState.Error(errorMessage), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Error when flow throws exception`() = runTest {
        coEvery { getUserByIdUseCase(1) } returns flow {
            throw RuntimeException("Falha no servidor")
        }

        viewModel = UserDetailsViewModel(getUserByIdUseCase, savedStateHandle)

        viewModel.state.test {
            assertEquals(UserDetailsState.Loading, awaitItem()) // inicial
            val error = awaitItem()
            assert(error is UserDetailsState.Error)
            assertEquals("Falha no servidor", (error as UserDetailsState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }
}