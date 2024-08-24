package example.com.sessions

data class ChatSession(
    val username: String,
    val sessionId: String,
    val channelId: String?
)
