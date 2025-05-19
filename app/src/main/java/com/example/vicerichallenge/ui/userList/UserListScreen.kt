package com.example.vicerichallenge.ui.userList

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vicerichallenge.ui.userList.components.ErrorView
import com.example.vicerichallenge.ui.userList.components.LoadingView
import com.example.vicerichallenge.ui.userList.components.UserList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    viewModel: UserListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Usuários") },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Atualizar")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            if (state.isLoading && state.users.isEmpty()) {
                LoadingView()
            } else if (state.errorMessage != null && state.errorMessage!!.isNotEmpty()) {
                ErrorView(
                    message = state.errorMessage!!,
                    onRetry = { viewModel.fetchUsers(false) }
                )
            } else {
                UserList(
                    users = state.users,
                    isRefreshing = state.isLoading,
                    onRefresh = viewModel::refresh,
                    onLoadMoreClick = viewModel::loadMoreUsers,
                    endReached = state.endReached,
                    onUserClick = {
                        //todo: navigate to user details
                    }
                )
            }
        }
    }
}
