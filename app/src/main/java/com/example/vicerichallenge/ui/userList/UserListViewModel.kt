package com.example.vicerichallenge.ui.userList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vicerichallenge.core.util.Resource
import com.example.vicerichallenge.domain.model.User
import com.example.vicerichallenge.domain.usecase.GetUsersUseCase
import com.example.vicerichallenge.domain.usecase.SearchUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserListState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val users: List<User> = emptyList(),
    val currentPage: Int = 1,
    val endReached: Boolean = false,
)

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val searchUsersUseCase: SearchUsersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<UserListState>(UserListState(isLoading = true))
    val state: StateFlow<UserListState> = _state.asStateFlow()

    private var searchJob: Job? = null
    private var getUsersJob: Job? = null

    init {
        fetchUsers(forceRefresh = false)
    }

    fun fetchUsers(forceRefresh: Boolean) {
        getUsersJob?.cancel()
        getUsersJob = viewModelScope.launch {
            if (forceRefresh.not() && _state.value.endReached) return@launch

            val currentPage = if (forceRefresh) 1 else _state.value.currentPage

            getUsersUseCase(forceRefresh, currentPage)
                .catch { e ->
                    _state.update { it.copy(errorMessage = (e.message ?: "Erro desconhecido")) }
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> _state.update { it.copy(isLoading = true) }
                        is Resource.Success -> {
                            val newUsers = resource.data.items
                            val totalItems = resource.data.totalItems

                            _state.update {
                                val allUsers = if (forceRefresh) newUsers else it.users + newUsers

                                it.copy(
                                    users = allUsers,
                                    isLoading = false,
                                    currentPage = currentPage + 1,
                                    endReached = allUsers.size >= totalItems,
                                    errorMessage = null
                                )
                            }
                        }
                        is Resource.Error -> _state.update { it.copy(errorMessage = (resource.message)) }
                    }
                }
        }
    }

    fun refresh() {
        fetchUsers(forceRefresh = true)
    }

    fun loadMoreUsers() {
        fetchUsers(forceRefresh = false)
    }

    fun search(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchUsersUseCase(query)
                .catch { e ->
                    _state.update { it.copy(error(e.message ?: "Erro desconhecido")) }
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> _state.update { it.copy(isLoading = true) }
                        is Resource.Success -> _state.value = UserListState(users = resource.data)
                        is Resource.Error -> _state.update { it.copy(errorMessage = (resource.message), isLoading = false) }
                    }
                }
        }
    }
}