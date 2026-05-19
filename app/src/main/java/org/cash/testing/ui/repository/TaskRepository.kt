package org.cash.testing.ui.repository

import org.cash.testing.ui.model.Priority
import org.cash.testing.ui.model.Task
import java.util.concurrent.CopyOnWriteArrayList

class TaskRepository {
    companion object {
        private val tasks = CopyOnWriteArrayList<Task>(
            listOf(
                Task(
                    id = 1,
                    title = "Revisar UAM - Cesar Silva",
                    description = "Revisar que no haya tareas pendientes en uamv",
                    priority = Priority.MEDIUM,
                    completed = false
                )
            )
        )
    }

    fun getTasks(): List<Task> = tasks.toList()

    fun getTaskById(id: Int): Task? = tasks.find { it.id == id }

    fun addTask(task: Task): Boolean {
        return tasks.add(task)
    }

    fun updateTask(updatedTask: Task): Boolean {
        val index = tasks.indexOfFirst { it.id == updatedTask.id }
        return if (index != -1) {
            tasks[index] = updatedTask
            true
        } else {
            false
        }
    }

    fun deleteTask(id: Int): Boolean {
        return tasks.removeIf { it.id == id }
    }
}
