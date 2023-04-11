package com.innaval.agendaeventos.presentation.ui.eventlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.innaval.agendaeventos.domain.model.EventBO
import com.innaval.agendaeventos.domain.usecases.FetchEventsUseCase
import com.innaval.agendaeventos.state.EventResponse
import com.innaval.agendaeventos.state.mapErrorToState
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class EventsViewModel @ViewModelInject constructor(
    private val eventsUseCase: FetchEventsUseCase,
    private val scheduler: Scheduler
) : ViewModel() {

    private val _eventsLiveData = MutableLiveData<EventResponse<List<EventBO>>>()
    val eventsLiveData get(): LiveData<EventResponse<List<EventBO>>> = _eventsLiveData
    private val disposables = CompositeDisposable()

    fun listEvents() {
        val disposable = eventsUseCase.execute()
            .subscribeOn(scheduler)
            .doOnSubscribe {
                _eventsLiveData.postValue(EventResponse.EventLoading())
            }.subscribe({ event ->
                _eventsLiveData.postValue(EventResponse.EventSuccess(event))
            }, { error ->
                _eventsLiveData.postValue(mapErrorToState(error))
            })
        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}