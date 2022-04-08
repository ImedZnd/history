package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework.event.repository

import kotlinx.coroutines.flow.count
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
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
    @Autowired private val reactiveDatabaseRepository: DatabaseRepository
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
    fun `Empty if repository is have one element`() {
        runBlocking{
            generateSequence(1) { it + 1 }
                .take(5)
                .map {
                    EventDAO(
                        Event.EventAction.SAVEUSER,
                        "x",
                        LocalDateTime.now())
                }
                .filter { it.toEvent().isRight }
                .forEach {
                    reactiveDatabaseRepository
                        .saveEvent(it.toEvent().get())
                }
            val result =
                reactiveDatabaseRepository
                    .findAllEvents().count()
            assert(result == 1)
        }
    }
}