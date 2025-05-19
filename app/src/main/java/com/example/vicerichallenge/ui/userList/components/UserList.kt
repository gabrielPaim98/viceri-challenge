package com.example.vicerichallenge.ui.userList.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.vicerichallenge.domain.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserList(
    users: List<User>,
    onUserClick: (Int) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(users) { user ->
                    UserListItem(user = user, onClick = { onUserClick(user.id) })
                    HorizontalDivider()
                }
            }
            Button(onClick = {}) { Text("Carregar Mais") }
        }
    }
}

@Composable
fun UserListItem(
    user: User,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        headlineContent = { Text(user.name) },
        supportingContent = { Text(user.email) },
        overlineContent = { Text(user.address.city) },
        leadingContent = {
            Icon(
                Icons.Filled.AccountCircle,
                contentDescription = "Localized description", //todo: localize description
            )
        }
    )
}

