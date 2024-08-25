package example.com.data

import example.com.data.model.Message

interface MessageDataSource {

    suspend fun getAllMessages(): List<Message>

    suspend fun getMessagesForChannel(channelId: String): List<Message>

    suspend fun getMessageForUser(userId: String): List<Message>

    suspend fun insertMessage(message: Message)

    suspend fun removeMessage(messageId: Int): Boolean
}