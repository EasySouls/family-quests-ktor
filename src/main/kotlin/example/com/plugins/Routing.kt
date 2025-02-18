package example.com.plugins

import example.com.room.RoomController
import example.com.routes.chatSocket
import example.com.routes.getAllMessages
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val roomController by inject<RoomController>()
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        chatSocket(roomController)
        getAllMessages(roomController)
    }
}
