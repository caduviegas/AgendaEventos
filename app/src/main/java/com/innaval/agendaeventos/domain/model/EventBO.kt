package com.innaval.agendaeventos.domain.model

import java.util.*

data class EventBO (
    val id: String,
    val title: String,
    val description: String,
    val people: List<PersonBO>?,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double,
    val price: Double,
    val date: Date
        )