package com.innaval.agendaeventos.domain.repository

import com.innaval.agendaeventos.domain.model.CheckinBO
import com.innaval.agendaeventos.domain.model.EventBO
import io.reactivex.Single

interface EventsRepository {

    fun getEventInfo(eventId: String): Single<EventBO>
    fun getEvents(): Single<List<EventBO>>
    fun checkinEvent(id: String, email: String, name: String): Single<CheckinBO>
}