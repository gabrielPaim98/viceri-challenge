package com.example.vicerichallenge.ui.userDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(
    navController: NavController,
    viewModel: UserDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Usuário") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (state) {
            is UserDetailsState.Loading -> CircularProgressIndicator()
            is UserDetailsState.Error -> Text((state as UserDetailsState.Error).message)
            is UserDetailsState.Success -> {
                val user = (state as UserDetailsState.Success).user

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "👤 ${user.name} (@${user.username})",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(text = "📧 Email: ${user.email}")
                    Text(text = "📱 Telefone: ${user.phone}")
                    Text(text = "🌐 Website: ${user.website}")

                    HorizontalDivider()

                    Text(text = "🏠 Endereço", style = MaterialTheme.typography.titleMedium)
                    Text(text = "${user.address.street}, ${user.address.suite}")
                    Text(text = "${user.address.city} - ${user.address.zipcode}")
                    Text(text = "📍 Geo: ${user.address.geo.lat}, ${user.address.geo.lng}")

                    HorizontalDivider()

                    Text(text = "🏢 Empresa", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Nome: ${user.company.name}")
                    Text(text = "Slogan: \"${user.company.catchPhrase}\"")
                    Text(text = "Área: ${user.company.bs}")
                }
            }
        }
    }
}