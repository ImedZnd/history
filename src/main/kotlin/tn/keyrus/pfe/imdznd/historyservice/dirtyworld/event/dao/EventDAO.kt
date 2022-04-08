package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.dao

import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "event")
@Entity(name = "event")
data class EventDAO(
    @Id
    val action: Event.EventAction = Event.EventAction.SAVEUSER,
    val objectId: String = "",
    val eventTime: LocalDateTime = LocalDateTime.now()
) {

    fun toEvent() =
        Event.of(
            this.action,
            this.objectId,
            this.eventTime
        )

    companion object {

        fun Event.toDAO() =
            EventDAO(
                action = action,
                objectId = objectId,
                eventTime = eventTime
            )

    }

}
