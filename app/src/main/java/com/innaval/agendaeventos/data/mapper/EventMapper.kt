package com.innaval.agendaeventos.data.mapper

import com.innaval.agendaeventos.data.model.EventDTO
import com.innaval.agendaeventos.domain.model.EventBO
import java.util.Date

fun EventDTO.toBO(): EventBO =
    EventBO(
        title = title,
        description = description,
        imageUrl = image,
        latitude = latitude,
        longitude = longitude,
        people = null,
        price = price,
        date = Date(date),
        id = id
    )