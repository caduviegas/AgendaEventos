package com.innaval.agendaeventos.domain

import androidx.lifecycle.Observer
import com.innaval.agendaeventos.domain.model.EventBO
import com.innaval.agendaeventos.domain.usecases.FetchEventsUseCase
import com.innaval.agendaeventos.presentation.ui.eventlist.EventsViewModel
import com.innaval.agendaeventos.state.EventResponse
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import io.reactivex.Single
import okhttp3.internal.EMPTY_RESPONSE
import org.junit.Assert
import retrofit2.HttpException
import retrofit2.Response

object EventsViewModelRobot {
    class EventsViewModelArrange {

        fun fetchEventsSuccess(
            eventsUseCase: FetchEventsUseCase
        ) {
            every { eventsUseCase.execute() } returns Single.just(mockEvents())
                .map { list ->
                    list.map { event -> event.toBO() }
                }
        }

        fun fetchEventsError(
            eventsUseCase: FetchEventsUseCase
        ) {
            every { eventsUseCase.execute() } returns Single.error(
                HttpException(Response.error<String>(404, EMPTY_RESPONSE))
            )
        }

        infix fun act(func: EventsViewModelAct.() -> Unit) =
            EventsViewModelAct().apply(func)
    }

    class EventsViewModelAct {

        fun observeEvents(
            viewModel: EventsViewModel,
            observer: Observer<EventResponse<List<EventBO>>>
        ) = viewModel.eventsLiveData.observeForever(observer)

        infix fun assert(func: EventsViewModelAssert.() -> Unit) =
            EventsViewModelAssert().apply(func)
    }

    class EventsViewModelAssert {

        fun successFetchEvents(observer: Observer<EventResponse<List<EventBO>>>) {
            val slot = slot<EventResponse<List<EventBO>>>()
            verify { observer.onChanged(capture(slot)) }
            Assert.assertTrue(slot.captured is EventResponse.EventSuccess)
        }

        fun failedFetchEvents(observer: Observer<EventResponse<List<EventBO>>>) {
            val slot = slot<EventResponse<List<EventBO>>>()
            verify { observer.onChanged(capture(slot)) }
            Assert.assertTrue(slot.captured is EventResponse.NetworkError)
        }

    }

    infix fun arrange(func: EventsViewModelArrange.() -> Unit) =
        EventsViewModelArrange().apply(func)
}