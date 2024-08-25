package example.com.data.model

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object MessagesTable : IntIdTable("messages") {
    val text: Column<String> = varchar("text", 255)
    val username: Column<String> = varchar("username", 50)
    val channelId: Column<String> = varchar("channelId", 50)
    val timestamp: Column<Instant> = timestamp("timestamp")
}

class MessageDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MessageDao>(MessagesTable)

    var text by MessagesTable.text
    var username by MessagesTable.username
    var channelId by MessagesTable.channelId
    var timestamp by MessagesTable.timestamp

    fun toMessage() = Message(
        id = id.value,
        text = text,
        username = username,
        channelId = channelId,
        timestamp = timestamp
    )
}
