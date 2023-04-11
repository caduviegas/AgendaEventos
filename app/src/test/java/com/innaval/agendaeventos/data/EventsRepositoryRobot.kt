package com.innaval.agendaeventos.data

import android.accounts.NetworkErrorException
import com.innaval.agendaeventos.data.repository.EventsRepositoryImpl
import com.innaval.agendaeventos.domain.model.EventBO
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single

object EventsRepositoryRobot {

    private val api = mockk<EventsApi>()
    private val subject = EventsRepositoryImpl(
        api = api
    )

    class CodewarsRepositoryArrange{

        fun mockUserFromApi(){
            every {
                api.events()
            } returns Single.just(mockEvents())
        }

        fun mockNetworkError(){
            every {
                api.events()
            } returns Single.error(NetworkErrorException())
        }

        infix fun act(func: CodewarsRepositoryAct.() -> Unit) =
            CodewarsRepositoryAct().apply(func)
    }

    class CodewarsRepositoryAct{

        fun getEvents(): Single<List<EventBO>> {
            return subject.getEvents()
        }

        infix fun assert(func: CodewarsRepositoryAssert.() -> Unit){
            CodewarsRepositoryAssert().apply(func)
        }
    }

    class CodewarsRepositoryAssert{

        fun success(observer: Single<List<EventBO>>){
            observer.test()
                .assertComplete()
                .assertValue { it.size == 2 }
        }

        fun errorListingEvents(observer: Single<List<EventBO>>){
            observer.test()
                .assertError(NetworkErrorException::class.java)
        }

    }

    infix fun arrange(func: CodewarsRepositoryArrange.() -> Unit) =
        CodewarsRepositoryArrange().apply(func)
}
}