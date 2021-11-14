package com.example.combattracker

import android.app.Activity
import android.content.res.Resources
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.combattracker.model.Entity
import com.example.combattracker.ui.theme.CombatTrackerTheme

@ExperimentalFoundationApi
@Composable
fun CharacterSheetBody(controller: NavController, players: List<Entity>) {
    val activity = LocalContext.current as? Activity
    Column {
        Text(text = "This is the Character Sheet view")
        Button(
            onClick = { activity?.finish() },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Exit")
        }
        DisplayCharacterList(players = players)
    }
    
}

@ExperimentalFoundationApi
@Composable
fun DisplayCharacterList(players: List<Entity>) {
    LazyColumn(Modifier.fillMaxWidth()) {
        stickyHeader {
            PlayerHeader()
            Divider()
        }

        items(players.sortedWith(compareBy ({-it.initiative}, {-it.attributes.dexterity}))) { player ->
            PlayerRow(player)
        }
    }
}

@Composable
fun PlayerHeader() {
    Row (modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colors.background)
        .padding(8.dp)) {
        Text("Name", modifier = Modifier.weight(1f))
        Text("Initiative", modifier = Modifier.weight(1f))
        Text("HP", modifier = Modifier.weight(1f))
        Text("FP", modifier = Modifier.weight(1f))
    }
}

@Composable
fun PlayerRow(player: Entity){
    Row (modifier = Modifier
        .height(48.dp)
        .fillMaxWidth()
        .padding(8.dp)) {
        Text(player.name, modifier = Modifier.weight(1f))
        Text("${player.initiative}", modifier = Modifier.weight(1f))
        Text("${player.healthCurrent}/${player.healthTotal}", modifier = Modifier.weight(1f))
        Text("${player.focusCurrent}/${player.focusTotal}", modifier = Modifier.weight(1f))
    }
}