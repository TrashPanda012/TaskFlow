package org.cash.testing.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object TaskList

@Serializable
data class TaskDetail(
    val taskId: Int? = null
)
