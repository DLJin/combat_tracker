package com.example.combattracker

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.combattracker.model.Entity
import com.example.combattracker.ui.theme.CombatTrackerTheme

class PlayerView : ComponentActivity() {

    val ixar = Entity(
        attributes = Entity.Attributes(13, 12, 8, 10, 15, 14),
        movement = 30,
        healthFactor = 12.0,
        focusFactor = 6.5
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CombatTrackerTheme {
                DisplayPlayer(player = ixar)
            }
        }
    }
}

@Composable
fun DisplayPlayer(player: Entity) {
    val activity = LocalContext.current as? Activity
    Column {
        Button(onClick = {activity?.finish()}) {
            Text("Back")
        }
        Row {
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("CON", style=MaterialTheme.typography.overline)
                Text(player.attributes.constitution.toString())
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("STR", style=MaterialTheme.typography.overline)
                Text(player.attributes.strength.toString())
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("DEX", style=MaterialTheme.typography.overline)
                Text(player.attributes.dexterity.toString())
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("WIS", style=MaterialTheme.typography.overline)
                Text(player.attributes.wisdom.toString())
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("INT", style=MaterialTheme.typography.overline)
                Text(player.attributes.intelligence.toString())
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("CHA", style=MaterialTheme.typography.overline)
                Text(player.attributes.charisma.toString())
            }
        }
        Text("HP: ${player.healthCurrent}/${player.healthTotal}")
        Text("FP: ${player.focusCurrent}/${player.focusTotal}")
        Text("Movement: ${player.movement}")
    }
}