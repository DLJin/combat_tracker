package com.example.combattracker.model

import kotlin.math.ceil

class Entity(val attributes: Attributes, val movement: Int, private val healthFactor: Double, private val focusFactor: Double) {
    data class Attributes(val constitution: Int, val strength: Int, val dexterity: Int, val wisdom: Int, val intelligence: Int, val charisma: Int)

    val healthTotal = ceil(healthFactor * attributes.constitution).toInt()
    val healthCurrent = healthTotal
    val focusTotal = ceil(focusFactor * attributes.intelligence).toInt()
    val focusCurrent = focusTotal

}