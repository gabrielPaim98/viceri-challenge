package com.example.vicerichallenge.domain.usecase

import com.example.vicerichallenge.core.util.PagedResult
import com.example.vicerichallenge.core.util.Resource
import com.example.vicerichallenge.domain.model.User
import com.example.vicerichallenge.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUsersUseCase(
    private val repository: UserRepository
) {
    operator fun invoke(forceRefresh: Boolean = false, currentPage: Int = 1, pageSize: Int = 5): Flow<Resource<PagedResult<User>>> {
        return repository.getUsers(forceRefresh, currentPage, pageSize)
    }
}