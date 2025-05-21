package com.example.vicerichallenge.domain.repository

import com.example.vicerichallenge.core.util.PagedResult
import com.example.vicerichallenge.core.util.Resource
import com.example.vicerichallenge.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUsers(forceRefresh: Boolean = false, currentPage: Int = 1, pageSize: Int = 5): Flow<Resource<PagedResult<User>>>

    fun getUserById(id: Int): Flow<Resource<User>>

    fun searchUsers(query: String): Flow<Resource<List<User>>>
}