package com.example.vicerichallenge.data.repository

import com.example.vicerichallenge.core.util.Resource
import com.example.vicerichallenge.data.local.UserDao
import com.example.vicerichallenge.data.mappers.toDomain
import com.example.vicerichallenge.data.mappers.toEntity
import com.example.vicerichallenge.data.remote.api.UserApi
import com.example.vicerichallenge.domain.model.User
import com.example.vicerichallenge.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val api: UserApi,
    private val dao: UserDao
) : UserRepository {

    override fun getUsers(forceRefresh: Boolean, currentPage: Int, pageSize: Int): Flow<Resource<List<User>>> = flow {
        emit(Resource.Loading())

        if (forceRefresh) {
            try {
                val usersFromApi = api.getUsers()
                dao.clearUsers()
                dao.insertUsers(usersFromApi.map { it.toEntity() })

            } catch (e: Exception) {
                emit(Resource.Error("Erro ao buscar da API: ${e.localizedMessage}"))
                return@flow
            }
        }

        val users = dao.getAllUsers().map { list -> list.map { it.toDomain() } }

        emitAll(users.map { Resource.Success(it) })
    }.catch { e ->
        emit(Resource.Error(e.localizedMessage ?: "Unexpected error"))
    }

    override fun getUserById(id: Int): Flow<Resource<User>> {
        return dao.getUserById(id)
            .map { entity ->
                entity?.let { Resource.Success(it.toDomain()) }
                    ?: Resource.Error("Usuário não encontrado com ID: $id")
            }
            .catch { e ->
                emit(Resource.Error("Erro ao buscar usuário: ${e.localizedMessage}"))
            }
    }

    override fun searchUsers(query: String): Flow<Resource<List<User>>> = flow {
        emit(Resource.Loading())

        val result = dao.searchUsers(query)
            .map { list -> list.map { it.toDomain() } }

        emitAll(result.map { Resource.Success(it) })
    }.catch { e ->
        emit(Resource.Error("Erro ao buscar usuários: ${e.localizedMessage}"))
    }
}