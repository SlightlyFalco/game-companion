package com.falco.gamecompanion.data.remote

import com.falco.gamecompanion.core.Result
import com.falco.gamecompanion.domain.models.Boss
import com.falco.gamecompanion.domain.models.EntityType
import com.falco.gamecompanion.domain.models.GameEntity
import com.falco.gamecompanion.domain.models.Item
import com.falco.gamecompanion.domain.models.Location
import com.falco.gamecompanion.domain.models.Npc
import com.falco.gamecompanion.domain.models.Quest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRemoteDataSource {

    private val quest1 = Quest(
        id = "quest-1",
        name = "The First Trial",
        summary = "Prove your worth by defeating the guardian at the old shrine.",
        sourceLinks = listOf("https://example.com/quests/first-trial"),
        relatedEntityIds = listOf("item-1", "npc-1", "location-1", "boss-1"),
        prerequisites = listOf("Complete the tutorial", "Reach level 5"),
        steps = listOf(
            "Travel to the Old Shrine",
            "Speak to the Guardian",
            "Defeat the Guardian in combat",
            "Return the proof to the village elder"
        ),
        rewards = listOf("500 gold", "Guardian's Sigil", "Experience")
    )

    private val quest2 = Quest(
        id = "quest-2",
        name = "Lost Artifact",
        summary = "Recover the stolen artifact from the bandit camp.",
        sourceLinks = listOf("https://example.com/quests/lost-artifact"),
        relatedEntityIds = listOf("item-2", "location-1"),
        prerequisites = listOf("The First Trial completed"),
        steps = listOf(
            "Find the bandit camp in the eastern woods",
            "Locate the artifact in the leader's tent",
            "Retrieve the artifact (stealth or combat)"
        ),
        rewards = listOf("300 gold", "Artifact", "Reputation")
    )

    private val item1 = Item(
        id = "item-1",
        name = "Guardian's Sigil",
        summary = "A token of victory from the shrine guardian. Used in crafting.",
        sourceLinks = listOf("https://example.com/items/guardian-sigil"),
        relatedEntityIds = listOf("quest-1", "boss-1")
    )

    private val item2 = Item(
        id = "item-2",
        name = "Ancient Artifact",
        summary = "A relic of the old kingdom. Required for the main story.",
        sourceLinks = listOf("https://example.com/items/ancient-artifact"),
        relatedEntityIds = listOf("quest-2", "location-1")
    )

    private val npc1 = Npc(
        id = "npc-1",
        name = "Village Elder",
        summary = "The elder of the starting village. Gives the First Trial quest.",
        sourceLinks = listOf("https://example.com/npcs/village-elder"),
        relatedEntityIds = listOf("quest-1", "location-1")
    )

    private val location1 = Location(
        id = "location-1",
        name = "Old Shrine",
        summary = "A ruined shrine in the northern hills. Home to the trial guardian.",
        sourceLinks = listOf("https://example.com/locations/old-shrine"),
        relatedEntityIds = listOf("quest-1", "quest-2", "boss-1", "npc-1")
    )

    private val boss1 = Boss(
        id = "boss-1",
        name = "Shrine Guardian",
        summary = "The stone guardian that protects the Old Shrine. Defeat it to complete The First Trial.",
        sourceLinks = listOf("https://example.com/bosses/shrine-guardian"),
        relatedEntityIds = listOf("quest-1", "item-1", "location-1")
    )

    private val allEntities: List<GameEntity> = listOf(
        quest1, quest2, item1, item2, npc1, location1, boss1
    )

    fun getAllEntities(): Flow<Result<List<GameEntity>>> = flow {
        emit(Result.Success(emptyList()))
        delay(800)
        emit(Result.Success(allEntities))
    }

    suspend fun getEntityById(id: String): Result<GameEntity?> {
        delay(400)
        return Result.Success(allEntities.find { it.id == id })
    }

    suspend fun getEntitiesByType(type: EntityType): Result<List<GameEntity>> {
        delay(500)
        return Result.Success(allEntities.filter { it.type == type })
    }
}
