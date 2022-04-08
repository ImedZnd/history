package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework.event.repository

import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.repository.EventRepository
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.dao.EventDAO
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.repository.ReactiveDatabaseRepository
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework.initializer.Initializer
import java.time.LocalDateTime
import java.util.function.Predicate
import java.util.stream.IntStream

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ContextConfiguration(initializers = [Initializer::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ReactiveDatabaseRepositoryTest @Autowired constructor(
    private val reactiveRepository: ReactiveDatabaseRepository
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
        val event = EventDAO(action, objectId, localtime)
        val source = reactiveRepository.save(event)
        StepVerifier.create(source)
            .expectNext(event)
            .verifyComplete()
    }

    @Test
    fun `Empty if repository is have one element`() {
        val action = Event.EventAction.SAVEUSER
        val objectId = "objectId"
        val localtime = LocalDateTime.now()
        val event = EventDAO(action, objectId, localtime)
        print("before test")
        val x = reactiveRepository.save(event)
        val source = reactiveRepository.save(event)
    }

    @Test
    fun `Flow with one Event if repository have only one`() {
        val size = 5
        val x = IntStream.iterate(1) { i: Int -> i + 1 }
            .limit(size.toLong())
            .boxed()
            .map {
                EventDAO()
            }
            .map { reactiveRepository.save(it) }
        val f: Long =1
        assertAll(
            { assert(reactiveRepository.findAll().count()== Mono.just(f)) },
        )
    }
}