package com.falco.gamecompanion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.falco.gamecompanion.domain.models.Item
import com.falco.gamecompanion.domain.models.Quest
import com.falco.gamecompanion.ui.theme.GameCompanionTheme
import com.falco.gamecompanion.domain.models.EntityType
import com.falco.gamecompanion.ui.components.EntityCard
import com.falco.gamecompanion.ui.viewmodel.EntityListViewModel
import com.falco.gamecompanion.ui.viewmodel.EntityListViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntityListScreen(
    entityType: EntityType,
    onEntityClick: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EntityListViewModel = viewModel(
        factory = EntityListViewModelFactory(entityType)
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(entityType.name.lowercase().replaceFirstChar { it.uppercase() }) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::setSearchQuery,
                label = { Text("Filter") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true
            )
            when (uiState.resource) {
                is com.falco.gamecompanion.core.Resource.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is com.falco.gamecompanion.core.Resource.Error -> {
                    Text(
                        text = (uiState.resource as com.falco.gamecompanion.core.Resource.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is com.falco.gamecompanion.core.Resource.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        items(
                            items = uiState.filteredEntities,
                            key = { it.id }
                        ) { entity ->
                            EntityCard(
                                entity = entity,
                                onClick = { onEntityClick(entity.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

private val sampleEntitiesForPreview = listOf(
    Quest(
        id = "quest-1",
        name = "The First Trial",
        summary = "Prove your worth by defeating the guardian at the old shrine.",
        sourceLinks = emptyList(),
        relatedEntityIds = emptyList(),
        prerequisites = emptyList(),
        steps = emptyList(),
        rewards = emptyList()
    ),
    Item(
        id = "item-1",
        name = "Guardian's Sigil",
        summary = "A token of victory from the shrine guardian.",
        sourceLinks = emptyList(),
        relatedEntityIds = emptyList()
    ),
    Quest(
        id = "quest-2",
        name = "Lost Artifact",
        summary = "Recover the stolen artifact from the bandit camp.",
        sourceLinks = emptyList(),
        relatedEntityIds = emptyList(),
        prerequisites = emptyList(),
        steps = emptyList(),
        rewards = emptyList()
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun EntityListScreenPreview() {
    GameCompanionTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Quests") })
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sampleEntitiesForPreview, key = { it.id }) { entity ->
                    EntityCard(
                        entity = entity,
                        onClick = {}
                    )
                }
            }
        }
    }
}
