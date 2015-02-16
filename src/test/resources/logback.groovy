import static java.util.logging.Level.FINEST

import java.util.logging.LogManager
import java.util.logging.Logger

import ch.qos.logback.classic.jul.LevelChangePropagator

import org.slf4j.bridge.SLF4JBridgeHandler

context = new LevelChangePropagator()
context.resetJUL = true

LogManager.getLogManager().reset()
SLF4JBridgeHandler.install()
Logger.getLogger("global").setLevel(FINEST)

appender("console", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "[%d{yyyy/MM/dd HH:mm:ss.SSS}] {%thread} %-5level in %logger: %msg%n"
    }
}

logger("com.foursquare4j", WARN)
logger("com.google.maps", WARN)
logger("org.apache.http", WARN)
logger("org.neo4j", WARN)
logger("org.springframework", WARN)

root(ALL, ["console"])