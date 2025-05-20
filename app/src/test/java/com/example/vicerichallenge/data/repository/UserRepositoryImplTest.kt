package com.example.vicerichallenge.data.repository

import com.example.vicerichallenge.core.util.Resource
import com.example.vicerichallenge.data.local.UserDao
import com.example.vicerichallenge.data.remote.api.UserApi
import com.example.vicerichallenge.fakes.FakeUser
import com.example.vicerichallenge.fakes.FakeUserDto
import com.example.vicerichallenge.fakes.FakeUserEntity
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class UserRepositoryImplTest {

    private lateinit var repository: UserRepositoryImpl
    private val api: UserApi = mockk()
    private val dao: UserDao = mockk()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = UserRepositoryImpl(api, dao)
    }

    @Test
    fun `should return data from API and update local cache when forceRefresh is true`() = runTest {
        val userDto = FakeUserDto.default
        val userEntity = FakeUserEntity.default

        coEvery { api.getUsers() } returns listOf(userDto)
        coEvery { dao.clearUsers() } just Runs
        coEvery { dao.insertUsers(any()) } just Runs
        coEvery { dao.getUsersCount() } returns flowOf(1)
        coEvery { dao.getAllUsers(any(), any()) } returns flowOf(listOf(userEntity))

        val result =
            repository.getUsers(forceRefresh = true, currentPage = 1, pageSize = 5).toList()

        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Success)
        assertEquals(1, (result[1] as Resource.Success).data.totalItems)
    }

    @Test
    fun `should return data only from local database when forceRefresh is false`() = runTest {
        val userEntity = FakeUserEntity.default

        coEvery { dao.getUsersCount() } returns flowOf(2)
        coEvery { dao.getAllUsers(any(), any()) } returns flowOf(listOf(userEntity))

        val result =
            repository.getUsers(forceRefresh = false, currentPage = 1, pageSize = 5).toList()

        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Success)
        assertEquals(1, (result[1] as Resource.Success).data.items.size)
    }

    @Test
    fun `should emit error when API fails on forceRefresh`() = runTest {
        coEvery { api.getUsers() } throws IOException("Network error")
        coEvery { dao.getUsersCount() } returns flowOf(0)
        coEvery { dao.getAllUsers(any(), any()) } returns flowOf(emptyList())

        val result =
            repository.getUsers(forceRefresh = true, currentPage = 1, pageSize = 5).toList()

        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Error)
        assertTrue((result[1] as Resource.Error).message!!.contains("Erro ao buscar da API"))
    }

    @Test
    fun `should return user when found`() = runTest {
        val userEntity = FakeUserEntity.default
        val user = FakeUser.default

        coEvery { dao.getUserById(1) } returns flowOf(userEntity)

        val result = repository.getUserById(1).first()

        assertTrue(result is Resource.Success)
        assertEquals(user, (result as Resource.Success).data)
    }

    @Test
    fun `should emit error when user not found`() = runTest {
        coEvery { dao.getUserById(1) } returns flowOf(null)

        val result = repository.getUserById(1).first()

        assertTrue(result is Resource.Error)
    }

    @Test
    fun `should emit loading and then success for search`() = runTest {
        val userEntity = FakeUserEntity.default
        val user = FakeUser.default

        coEvery { dao.searchUsers("john") } returns flowOf(listOf(userEntity))

        val result = repository.searchUsers("john").toList()

        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Success)
        assertEquals(listOf(user), (result[1] as Resource.Success).data)
    }

    @Test
    fun `should emit error when exception occurs in search`() = runTest {
        coEvery { dao.searchUsers("john") } throws RuntimeException("DB failure")

        val result = repository.searchUsers("john").toList()

        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Error)
        assertTrue((result[1] as Resource.Error).message.contains("Erro ao buscar usuários"))
    }
}