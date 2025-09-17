package com.nsutrack.financetracker.data

import java.time.LocalDateTime

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val content: String,
    val role: MessageRole,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val isTyping: Boolean = false
)

enum class MessageRole {
    USER,
    ASSISTANT
}

data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val isTyping: Boolean = false,
    val error: String? = null
)