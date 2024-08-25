package example.com.data

import example.com.data.model.Message
import kotlinx.datetime.Clock

class FakeMessageDataSource : MessageDataSource {

    private val messages = mutableListOf<Message>(
        Message( 1,  "Hello", "Alice","1", Clock.System.now()),
        Message(2,"Hi", "Bob", "1", Clock.System.now()),
        Message(3,"Hey", "Charlie", "1", Clock.System.now()),
        Message(4,"Hola", "David", "1", Clock.System.now()),
        Message(5,"Bonjour", "Eve", "1", Clock.System.now()),
    )

    override suspend fun getAllMessages(): List<Message> = messages.sortedByDescending { it.timestamp }

    override suspend fun getMessagesForChannel(channelId: String): List<Message> {
        return messages.filter { it.channelId == channelId }.sortedByDescending { it.timestamp }
    }

    override suspend fun getMessageForUser(userId: String): List<Message> {
        return messages.filter { it.username == userId }.sortedByDescending { it.timestamp }
    }

    override suspend fun insertMessage(message: Message) {
        messages.add(message)
    }

    override suspend fun removeMessage(messageId: Int): Boolean {
        return messages.removeIf { it.id == messageId }
    }
}