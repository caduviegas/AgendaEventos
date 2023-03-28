package com.innaval.agendaeventos.domain.usecases

import com.innaval.agendaeventos.domain.model.CheckinBO
import com.innaval.agendaeventos.domain.repository.EventsRepository
import io.reactivex.Single

class CheckinUseCase(val repository: EventsRepository) {

    fun execute(params: Params): Single<CheckinBO>{
        return repository.checkinEvent(
            params.eventId,
            params.userEmail,
            params.userName
        )
    }

    data class Params(val eventId: String, val userEmail: String, val userName: String)
}