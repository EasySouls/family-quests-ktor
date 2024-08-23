package example.com.data.model

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Messages : IntIdTable() {
    val text: Column<String> = varchar("text", 255)
    val username: Column<String> = varchar("username", 50)
    val channelId: Column<String> = varchar("channelId", 50)
    val timestamp: Column<Instant> = timestamp("timestamp")
}

class Message(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Message>(Messages)

    var text by Messages.text
    var username by Messages.username
    var channelId by Messages.channelId
    var timestamp by Messages.timestamp

    fun toMessageDto() = MessageDto(
        id = id.value,
        text = text,
        username = username,
        channelId = channelId,
        timestamp = timestamp
    )
}
