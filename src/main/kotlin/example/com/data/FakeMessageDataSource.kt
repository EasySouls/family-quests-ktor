package example.com.data

import example.com.data.model.MessageDto
import kotlinx.datetime.Clock

class FakeMessageDataSource : MessageDataSource {

    private val messages = mutableListOf<MessageDto>(
        MessageDto( 1,  "Hello", "Alice","1", Clock.System.now()),
        MessageDto(2,"Hi", "Bob", "1", Clock.System.now()),
        MessageDto(3,"Hey", "Charlie", "1", Clock.System.now()),
        MessageDto(4,"Hola", "David", "1", Clock.System.now()),
        MessageDto(5,"Bonjour", "Eve", "1", Clock.System.now()),
    )

    override suspend fun getAllMessages(): List<MessageDto> = messages.sortedByDescending { it.timestamp }

    override suspend fun getMessagesForChannel(channelId: String): List<MessageDto> {
        return messages.filter { it.channelId == channelId }.sortedByDescending { it.timestamp }
    }

    override suspend fun getMessageForUser(userId: String): List<MessageDto> {
        return messages.filter { it.username == userId }.sortedByDescending { it.timestamp }
    }

    override suspend fun insertMessage(message: MessageDto) {
        messages.add(message)
    }

    override suspend fun removeMessage(messageId: Int): Boolean {
        return messages.removeIf { it.id == messageId }
    }
}