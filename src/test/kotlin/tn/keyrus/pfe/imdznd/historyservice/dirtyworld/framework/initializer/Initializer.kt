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
        val postgresUrl: String = (postgreSQLContainer.host + ":" + postgreSQLContainer.getMappedPort(5432))

        print("  spring.r2dbc.url=r2dbc:postgresql://$postgresUrl/events")
        TestPropertyValues
            .of(
                "spring.r2dbc.url=r2dbc:postgresql://$postgresUrl/events",
                "spring.r2dbc.username=" + "postgres",
                "spring.r2dbc.password=" + "changeme"
            )
            .applyTo(applicationContext.environment)

        val applicationContextCloseListener: ApplicationListener<ContextClosedEvent> =
            ApplicationListener<ContextClosedEvent> {
                postgreSQLContainer.stop()
                while (postgreSQLContainer.isRunning) Thread.sleep(3000)
            }
        applicationContext
            .addApplicationListener(applicationContextCloseListener)

    }
}
