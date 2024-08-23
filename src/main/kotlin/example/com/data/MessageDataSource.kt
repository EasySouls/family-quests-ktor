package example.com.data

import example.com.data.model.MessageDto

interface MessageDataSource {

    suspend fun getAllMessages(): List<MessageDto>

    suspend fun getMessagesForChannel(channelId: String): List<MessageDto>

    suspend fun getMessageForUser(userId: String): List<MessageDto>

    suspend fun insertMessage(message: MessageDto)

    suspend fun removeMessage(messageId: Int): Boolean
}