package org.cash.testing.ui.model

enum class Priority(val displayName: String) {
    HIGH("Alta"),
    MEDIUM("Media"),
    LOW("Baja")
}

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val priority: Priority = Priority.MEDIUM,
    val completed: Boolean = false
)
