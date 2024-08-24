package example.com.room

import example.com.data.MessageDataSource
import example.com.data.model.MessageDto
import io.ktor.websocket.*
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val messageDataSource: MessageDataSource
) {
//    private val rooms = ConcurrentHashMap<String, ConcurrentHashMap<String, Member>>()
    private val members = ConcurrentHashMap<String, Member>()

    fun onJoin(username: String, sessionId: String, socketSession: WebSocketSession) {
        if (members.containsKey(username)) {
            throw MemberAlreadyExistsException()
        }

        members[username] = Member(username, sessionId, socketSession)
    }

    suspend fun sendMessage(senderUsername: String, message: String, channelId: String) {
        members.values.forEach { member ->
            val messageDto = MessageDto(
                id = 0,
                text = message,
                username = senderUsername,
                channelId = channelId,
                timestamp = Clock.System.now()
            )
            messageDataSource.insertMessage(messageDto)

            val parsedMessage = Json.encodeToString(messageDto)
            member.socket.send(Frame.Text(parsedMessage))
        }
    }

    suspend fun getAllMessages(): List<MessageDto> {
        return messageDataSource.getAllMessages()
    }

    suspend fun tryDisconnect(username: String) {
        members[username]?.socket?.close()
        members.remove(username)
    }
}