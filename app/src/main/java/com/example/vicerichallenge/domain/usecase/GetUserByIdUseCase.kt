package com.example.vicerichallenge.domain.usecase

import com.example.vicerichallenge.core.util.Resource
import com.example.vicerichallenge.domain.model.User
import com.example.vicerichallenge.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow


class GetUserByIdUseCase(
    private val repository: UserRepository
) {
    operator fun invoke(id: Int): Flow<Resource<User>> {
        return repository.getUserById(id)
    }
}