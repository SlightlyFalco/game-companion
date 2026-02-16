package com.falco.gamecompanion.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.falco.gamecompanion.ui.theme.GameCompanionTheme
import com.falco.gamecompanion.domain.models.GameEntity
import com.falco.gamecompanion.ui.viewmodel.EntityDetailViewModel
import com.falco.gamecompanion.ui.viewmodel.EntityDetailViewModelFactory
import com.falco.gamecompanion.domain.models.Quest
import com.falco.gamecompanion.ui.components.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntityDetailScreen(
    entityId: String,
    onRelatedEntityClick: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EntityDetailViewModel = viewModel(
        factory = EntityDetailViewModelFactory(entityId)
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(uiState.entity?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { innerPadding ->
        when (uiState.resource) {
            is com.falco.gamecompanion.core.Resource.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
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
                    modifier = Modifier.padding(innerPadding).padding(16.dp)
                )
            }
            is com.falco.gamecompanion.core.Resource.Success -> {
                uiState.entity?.let { entity ->
                    EntityDetailContent(
                        entity = entity,
                        relatedNames = uiState.relatedEntities,
                        onRelatedClick = onRelatedEntityClick,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EntityDetailContent(
    entity: GameEntity,
    relatedNames: Map<String, String>,
    onRelatedClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = entity.summary,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (entity is Quest) {
            if (entity.prerequisites.isNotEmpty()) {
                SectionHeader("Prerequisites")
                entity.prerequisites.forEach { step ->
                    Text(
                        text = "• $step",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
            if (entity.steps.isNotEmpty()) {
                SectionHeader("Steps")
                entity.steps.forEachIndexed { index, step ->
                    Text(
                        text = "${index + 1}. $step",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
            if (entity.rewards.isNotEmpty()) {
                SectionHeader("Rewards")
                entity.rewards.forEach { reward ->
                    Text(
                        text = "• $reward",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
        if (relatedNames.isNotEmpty()) {
            SectionHeader("Related")
            relatedNames.forEach { (id, name) ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { onRelatedClick(id) }
                        .padding(vertical = 4.dp)
                )
            }
        }
        if (entity.sourceLinks.isNotEmpty()) {
            SectionHeader("Sources")
            entity.sourceLinks.forEach { url ->
                Text(
                    text = url,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

private val sampleEntityForDetailPreview = Quest(
    id = "quest-1",
    name = "The First Trial",
    summary = "Prove your worth by defeating the guardian at the old shrine.",
    sourceLinks = listOf("https://example.com/quests/first-trial"),
    relatedEntityIds = listOf("item-1", "boss-1"),
    prerequisites = listOf("Complete the tutorial", "Reach level 5"),
    steps = listOf(
        "Travel to the Old Shrine",
        "Speak to the Guardian",
        "Defeat the Guardian in combat"
    ),
    rewards = listOf("500 gold", "Guardian's Sigil")
)

private val sampleRelatedNamesForPreview = mapOf(
    "item-1" to "Guardian's Sigil",
    "boss-1" to "Shrine Guardian"
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun EntityDetailScreenPreview() {
    GameCompanionTheme {
        EntityDetailContent(
            entity = sampleEntityForDetailPreview,
            relatedNames = sampleRelatedNamesForPreview,
            onRelatedClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
