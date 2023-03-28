package com.innaval.agendaeventos.domain.usecases

import com.innaval.agendaeventos.domain.model.EventBO
import com.innaval.agendaeventos.domain.repository.EventsRepository
import io.reactivex.Single

class FetchEventsUseCase(val repository: EventsRepository) {

    fun execute(): Single<List<EventBO>>{
        return repository.getEvents()
    }
}