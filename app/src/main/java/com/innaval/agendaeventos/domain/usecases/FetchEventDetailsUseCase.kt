package com.innaval.agendaeventos.domain.usecases

import com.innaval.agendaeventos.domain.model.EventBO
import com.innaval.agendaeventos.domain.repository.EventsRepository
import io.reactivex.Single

class FetchEventDetailsUseCase(
    val repository: EventsRepository
) {

    fun execute(params: Params): Single<EventBO>{
        return repository.getEventInfo(params.eventId)
    }

    data class Params(val eventId: String)
}