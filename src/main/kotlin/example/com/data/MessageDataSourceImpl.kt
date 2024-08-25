package example.com.data

import example.com.data.model.MessageDao
import example.com.data.model.Message
import example.com.data.model.MessagesTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class MessageDataSourceImpl(
    database: Database
) : MessageDataSource {

    init {
        transaction(database) {
            SchemaUtils.create(MessagesTable)
        }
    }

    override suspend fun getAllMessages(): List<Message> {
        return dbQuery {
            MessageDao.all().map { it.toMessage() }.sortedByDescending { it.timestamp }
        }
    }

    override suspend fun getMessagesForChannel(channelId: String): List<Message> {
        return dbQuery {
            MessageDao.find { MessagesTable.channelId eq channelId }
                .map { it.toMessage() }
                .sortedByDescending { it.timestamp }
            }
    }

    override suspend fun getMessageForUser(userId: String): List<Message> {
        return dbQuery {
            MessageDao.find { MessagesTable.username eq userId }.map { it.toMessage() }.sortedByDescending { it.timestamp }
        }
    }

    override suspend fun insertMessage(message: Message): Unit = dbQuery {
        MessageDao.new {
            text = message.text
            username = message.username
            channelId = message.channelId
            timestamp = message.timestamp
        }
    }

    override suspend fun removeMessage(messageId: Int): Boolean =
        dbQuery {
            val message = MessageDao.findById(messageId) ?: return@dbQuery false
            message.delete()
            true
        }
}

private suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }
