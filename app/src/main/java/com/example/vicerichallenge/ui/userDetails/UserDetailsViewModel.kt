package com.example.vicerichallenge.ui.userDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vicerichallenge.core.util.Resource
import com.example.vicerichallenge.domain.model.User
import com.example.vicerichallenge.domain.usecase.GetUserByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserDetailsState {
    object Loading : UserDetailsState()
    data class Success(val user: User) : UserDetailsState()
    data class Error(val message: String) : UserDetailsState()
}

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId: Int = checkNotNull(savedStateHandle["userId"])

    private val _state = MutableStateFlow<UserDetailsState>(UserDetailsState.Loading)
    val state: StateFlow<UserDetailsState> = _state.asStateFlow()

    private var getUserDetailsJob: Job? = null

    init {
        getUserDetails()
    }

    fun getUserDetails() {
        getUserDetailsJob?.cancel()
        getUserDetailsJob = viewModelScope.launch {
            getUserByIdUseCase(userId)
                .catch { e ->
                    _state.value = UserDetailsState.Error(e.message ?: "Erro desconhecido")
                }.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> _state.value = UserDetailsState.Loading
                        is Resource.Error -> _state.value = UserDetailsState.Error(resource.message)
                        is Resource.Success -> _state.value =
                            UserDetailsState.Success(resource.data)
                    }
                }
        }
    }
}