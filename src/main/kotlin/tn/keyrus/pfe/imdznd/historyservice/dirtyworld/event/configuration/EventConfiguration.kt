package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.repository.EventRepository
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.repository.DatabaseRepository
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.repository.ReactiveDatabaseRepository

@Configuration
class EventConfiguration {

    @Bean
    fun databaseRepository(reactiveDatabaseRepository: ReactiveDatabaseRepository): EventRepository {
        return DatabaseRepository(reactiveDatabaseRepository)
    }

}