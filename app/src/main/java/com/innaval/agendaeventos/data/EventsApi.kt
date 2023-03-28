package com.innaval.agendaeventos.data

import com.innaval.agendaeventos.data.model.CheckinRequestDTO
import com.innaval.agendaeventos.data.model.EventDTO
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventsApi {

    @GET("api/evets/{eventId}")
    fun event(@Path("eventId") eventId: String): Single<EventDTO>

    @GET("api/events")
    fun events(): Single<List<EventDTO>>

    @POST("api/checkin")
    fun checkin(@Body checkinRequest: CheckinRequestDTO): Single<Any>
}