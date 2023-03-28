package com.innaval.agendaeventos.data.repository

import com.innaval.agendaeventos.data.EventsApi
import com.innaval.agendaeventos.data.mapper.toBO
import com.innaval.agendaeventos.data.model.CheckinRequestDTO
import com.innaval.agendaeventos.domain.model.CheckinBO
import com.innaval.agendaeventos.domain.model.EventBO
import com.innaval.agendaeventos.domain.repository.EventsRepository
import io.reactivex.Single

class EventsRepositoryImpl(
    private val api: EventsApi
) : EventsRepository {
    override fun getEventInfo(eventId: String): Single<EventBO> {
        return api.event(eventId).map { it.toBO() }
    }

    override fun getEvents(): Single<List<EventBO>> {
        return api.events().map { it -> it.map { it.toBO() } }
    }

    override fun checkinEvent(id: String, email: String, name: String): Single<CheckinBO> {
        return api.checkin(CheckinRequestDTO(id, name, email)).map { CheckinBO(id, name, email) }
    }
}