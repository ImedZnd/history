package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework.initializer

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ApplicationListener
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextClosedEvent
import org.testcontainers.containers.PostgreSQLContainer

@Configuration
class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val postgresqlContainer =
            PostgreSQLContainer("postgres:12-alpine")
                .withExposedPorts(5432)
                .withDatabaseName("events")
                .withUsername("postgres")
                .withPassword("postgres")

        postgresqlContainer.start()
        val postgresUrl: String = (postgresqlContainer.host + ":" + postgresqlContainer.getMappedPort(5432))
        print(postgresUrl)
        print("  spring.r2dbc.url=r2dbc:postgresql://$postgresUrl/events")
        TestPropertyValues
            .of(
                "spring.r2dbc.url=r2dbc:postgresql://$postgresUrl/events",
                "spring.r2dbc.username=" + "postgres",
                "spring.r2dbc.password=" + "postgres",
            )
            .applyTo(applicationContext.environment)

        val applicationContextCloseListener: ApplicationListener<ContextClosedEvent> =
            ApplicationListener<ContextClosedEvent> {
                postgresqlContainer.stop()
                while (postgresqlContainer.isRunning) Thread.sleep(3000)
            }
        applicationContext
            .addApplicationListener(applicationContextCloseListener)

    }
}
