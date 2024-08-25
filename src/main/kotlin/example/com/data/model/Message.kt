package example.com.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: Int,
    val text: String,
    val username: String,
    val channelId: String,
    val timestamp: Instant,
)
