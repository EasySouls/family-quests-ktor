package example.com.data

import example.com.data.model.Message
import example.com.data.model.MessageDto
import example.com.data.model.Messages
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class MessageDataSourceImpl(
    database: Database
) : MessageDataSource {

    init {
        transaction(database) {
            SchemaUtils.create(Messages)
        }
    }

    override suspend fun getAllMessages(): List<MessageDto> {
        return dbQuery {
            Messages.selectAll()
                .map {
                    MessageDto(
                        it[Messages.id].value,
                        it[Messages.text],
                        it[Messages.username],
                        it[Messages.channelId],
                        it[Messages.timestamp]
                    )
                }
        }
    }

    override suspend fun getMessagesForChannel(channelId: String): List<MessageDto> {
        return dbQuery {
            Messages.selectAll()
                .where { Messages.channelId eq channelId }
                .map {
                    MessageDto(
                        it[Messages.id].value,
                        it[Messages.text],
                        it[Messages.username],
                        it[Messages.channelId],
                        it[Messages.timestamp]
                    )
                }
        }
    }

    override suspend fun getMessageForUser(userId: String): List<MessageDto> {
        return dbQuery {
            Message.find { Messages.username eq userId }.map { it.toMessageDto() }
        }
    }

    override suspend fun insertMessage(message: MessageDto): Unit = dbQuery {
        Message.new {
            text = message.text
            username = message.username
            channelId = message.channelId
            timestamp = message.timestamp
        }
    }

    override suspend fun removeMessage(messageId: Int): Boolean =
        dbQuery {
            val message = Message.findById(messageId) ?: return@dbQuery false
            message.delete()
            true
        }
}

private suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }
