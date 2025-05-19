package com.example.vicerichallenge.domain.usecase

import com.example.vicerichallenge.core.util.Resource
import com.example.vicerichallenge.domain.model.User
import com.example.vicerichallenge.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class SearchUsersUseCase(
    private val repository: UserRepository
) {
    operator fun invoke(query: String): Flow<Resource<List<User>>> {
        return repository.searchUsers(query)
    }
}