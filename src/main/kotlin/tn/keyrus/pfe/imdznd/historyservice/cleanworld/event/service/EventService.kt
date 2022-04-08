package tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.service

import io.vavr.control.Either
import kotlinx.coroutines.flow.Flow
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.repository.EventRepository
import java.time.LocalDate

class EventService(
    private val eventRepository: EventRepository
    ){

    fun getAllEvents(): Flow<Event> {
        return eventRepository.findAllEvents()
    }

    fun getAllEventByAction(eventAction: Event.EventAction): Flow<Event> {
        return eventRepository.findAllEventByAction(eventAction)
    }

    fun getAllEventByObjectId(objectId:String): Flow<Event> {
        return eventRepository.findAllEventByObjectId(objectId)
    }

    fun getAllEventByCreationDate(startDate: LocalDate): Flow<Event> {
        return eventRepository.findAllEventByDateRange(startDate)
    }

    fun getAllEventBetweenMaxAndMin(startDate: LocalDate, endDate: LocalDate): Flow<Event> {
        return eventRepository.findAllEventByDateRange(startDate,endDate)
    }

    suspend fun saveEvent(event:Event):Either<EventServiceIOError,Event> {
        val result = eventRepository.saveEvent(event)
        return if (result.isRight) Either.right(result.get()) else Either.left(EventServiceIOError)
    }

    object EventServiceIOError
}