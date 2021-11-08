package com.example.combattracker

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.combattracker.model.Entity
import com.example.combattracker.ui.theme.CombatTrackerTheme

class PlayerView : ComponentActivity() {

    val ixar = Entity(
        name = "Ixar",
        attributes = Entity.Attributes(13, 12, 8, 10, 15, 14),
        movement = 30,
        healthFactor = 12.0,
        focusFactor = 6.5
    )

    val seren = Entity(
        name = "Seren",
        attributes = Entity.Attributes(8, 12, 15, 14, 13, 12),
        movement = 30,
        healthFactor = 11.0,
        focusFactor = 7.0
    )

    val elmo = Entity(
        name = "Elmo Elless",
        attributes = Entity.Attributes(13, 10, 14, 15, 12, 8),
        movement = 45,
        healthFactor = 13.0,
        focusFactor = 6.0
    )

    val players = mutableListOf(ixar, seren, elmo)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var expanded by remember { mutableStateOf(false) }
            var selectedPlayer by remember { mutableStateOf(ixar) }
            val icon = if (expanded) {
                Icons.Filled.ArrowDropUp
            } else {
                Icons.Filled.ArrowDropDown
            }
            CombatTrackerTheme {
                Column {
                    Button(
                        onClick = {finish()},
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Back")
                    }
                    Box(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        OutlinedTextField(
                            value = selectedPlayer.name,
                            onValueChange = { },
                            label = { Text("Player") },
                            trailingIcon = {
                                Icon(
                                    icon,
                                    "contentDescription",
                                    Modifier.clickable { expanded = !expanded })
                            },
                            singleLine = true,
                            readOnly = true
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            players.forEach { player ->
                                DropdownMenuItem(onClick = {
                                    selectedPlayer = player
                                    expanded = false
                                }) {
                                    Text(text = player.name)
                                }
                            }
                        }
                    }
                    DisplayPlayer(player = selectedPlayer)
                }
            }
        }
    }
}

@Composable
fun DisplayPlayer(player: Entity) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = player.name
        )
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
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
        Text(
            text = "HP: ${player.healthCurrent}/${player.healthTotal}"
        )
        Text(
            text = "FP: ${player.focusCurrent}/${player.focusTotal}"
        )
        Text(
            text = "Movement: ${player.movement}"
        )
    }
}