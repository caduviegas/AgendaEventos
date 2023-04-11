package com.innaval.agendaeventos.domain

import androidx.lifecycle.Observer
import com.innaval.agendaeventos.data.EventsRepositoryRobot.arrange
import com.innaval.agendaeventos.domain.model.EventBO
import com.innaval.agendaeventos.domain.usecases.FetchEventsUseCase
import com.innaval.agendaeventos.state.EventResponse
import io.mockk.mockk
import io.reactivex.Scheduler
import org.junit.Rule
import org.junit.Test

class EventsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val eventsUseCase = mockk<FetchEventsUseCase>()
    private val scheduler = mockk<Scheduler>()
    private val observer = mockk<Observer<EventResponse<List<EventBO>>>>()
    private val viewModel = EventsViewModel(eventsUseCase, scheduler)

    @Test
    fun `when fetch events should return events list`() {
        arrange {
            fetchEventsSuccess(eventsUseCase)
        } act {
            observeEvents(viewModel, observer)
        } assert {
            successFetchEvents(observer)
        }
    }

    @Test
    fun `when fetch events should return error`() {
        arrange {
            fetchEventsError(eventsUseCase)
        } act {
            observeEvents(viewModel, observer)
        } assert {
            failedFetchEvents(observer)
        }
    }

}