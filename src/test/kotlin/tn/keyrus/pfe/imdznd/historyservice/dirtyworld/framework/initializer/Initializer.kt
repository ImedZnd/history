package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework.initializer

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ApplicationListener
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextClosedEvent
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

@Configuration
class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val postgreSQLContainer =
            GenericContainer(DockerImageName.parse("postgres"))
                .withEnv("POSTGRES_USER", "postgres")
                .withEnv("POSTGRES_PASSWORD", "changeme")
                .withExposedPorts(5432)

        postgreSQLContainer.start()

        val postgresUrl: String = (postgreSQLContainer.host + ":" + postgreSQLContainer.portBindings)

        TestPropertyValues
            .of(
                "spring.datasource.url=jdbc:postgresql://$postgresUrl/events",
                "spring.datasource.username=" + "postgres",
                "spring.datasource.password=" + "changeme"
            )
            .applyTo(applicationContext.environment)

        if (postgreSQLContainer.isRunning) println("postgres is running")

        val applicationContextCloseListener: ApplicationListener<ContextClosedEvent> =
            ApplicationListener<ContextClosedEvent> {
                postgreSQLContainer.stop()
                while (postgreSQLContainer.isRunning) Thread.sleep(3000)
            }
        applicationContext
            .addApplicationListener(applicationContextCloseListener)

    }
}