package example.com.plugins

import example.com.data.MessageDataSource
import example.com.data.MessageDataSourceImpl
import example.com.room.RoomController
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureFrameworks() {
    install(Koin) {
        slf4jLogger()
        modules(
            module {
                single<Database> {
                    Database.connect(
                        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
                        user = "root",
                        driver = "org.h2.Driver",
                        password = "",
                    )
                }
            },
            module {
                single<MessageDataSource> {
                    MessageDataSourceImpl(database = get())
                }
            },
            module {
                single<RoomController> {
                    RoomController(messageDataSource = get())
                }
            }
        )
    }
}
