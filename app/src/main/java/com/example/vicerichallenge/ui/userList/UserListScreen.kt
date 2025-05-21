package com.example.vicerichallenge.ui.userList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vicerichallenge.ui.userList.components.ErrorView
import com.example.vicerichallenge.ui.userList.components.LoadingView
import com.example.vicerichallenge.ui.userList.components.SearchBar
import com.example.vicerichallenge.ui.userList.components.UserList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun UserListScreen(
    viewModel: UserListViewModel = hiltViewModel(),
    onUserClick: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var query by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(query) {
        snapshotFlow { query }.debounce(300).collectLatest {
            viewModel.search(it.text)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Usuários") }, actions = {
                IconButton(onClick = { viewModel.refresh() }) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Atualizar")
                }
            })
        }) { paddingValues ->

        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            SearchBar(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
            )

            if (state.isLoading && state.users.isEmpty()) {
                LoadingView()
            } else if (state.errorMessage != null && state.errorMessage!!.isNotEmpty()) {
                ErrorView(
                    message = state.errorMessage!!, onRetry = { viewModel.fetchUsers(false) })
            } else {
                UserList(
                    users = state.users,
                    isRefreshing = state.isLoading,
                    onRefresh = viewModel::refresh,
                    onLoadMoreClick = viewModel::loadMoreUsers,
                    endReached = state.endReached,
                    onUserClick = onUserClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}