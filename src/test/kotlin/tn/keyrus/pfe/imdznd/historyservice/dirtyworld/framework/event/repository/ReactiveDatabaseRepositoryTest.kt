package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework.event.repository

import kotlinx.coroutines.flow.count
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.dao.EventDAO
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.repository.DatabaseRepository
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework.initializer.Initializer
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ContextConfiguration(initializers = [Initializer::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ReactiveDatabaseRepositoryTest(
    private val reactiveDatabaseRepository: DatabaseRepository
) {
    @BeforeAll
    fun beforeAll() {
        println("beforeAll")
    }

    @BeforeEach
    fun beforeEach() {
        println("beforeEach")
    }

    @AfterEach
    fun afterEach() {
        println("afterEach")
    }

    @AfterAll
    fun afterAll() {
        println("afterAll")
    }

    @Test
    fun `Mono if repository is have one element`() {
        val action = Event.EventAction.SAVEUSER
        val objectId = "objectId"
        val localtime = LocalDateTime.now()
        val event = Event.of(action, objectId, localtime)
    }

    @Test
    suspend fun `Empty if repository is have one element`() {
        runBlocking{
            generateSequence(1) { it + 1 }
                .take(5)
                .map {
                    EventDAO()
                }
                .filter { it.toEvent().isRight }
                .forEach {
                    reactiveDatabaseRepository
                        .saveEvent(it.toEvent().get())
                }
            val result =
                reactiveDatabaseRepository
                    .findAllEvents()
            assert(result.count() == 1)
        }

    }
}