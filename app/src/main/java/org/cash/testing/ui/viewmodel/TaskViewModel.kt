package org.cash.testing.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.cash.testing.ui.model.Priority
import org.cash.testing.ui.model.Task
import org.cash.testing.ui.repository.TaskRepository

data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val selectedPriority: Priority? = null,
    val showCompleted: Boolean? = null
)

class TaskViewModel : ViewModel() {
    private val repository = TaskRepository()

    private val _uiState = MutableStateFlow(TaskListUiState())
    val uiState: StateFlow<TaskListUiState> = _uiState.asStateFlow()

    var titleInput by mutableStateOf("")
        private set

    var descriptionInput by mutableStateOf("")
        private set

    var priorityInput by mutableStateOf(Priority.MEDIUM)
        private set

    var completedInput by mutableStateOf(false)
        private set

    var isEditMode by mutableStateOf(false)
        private set

    var taskIdEditing by mutableStateOf<Int?>(null)
        private set

    var formError by mutableStateOf<String?>(null)
        private set

    init {
        refreshTasks()
    }

    // Task list actions
    fun refreshTasks() {
        val allTasks = repository.getTasks()
        val currentFilter = _uiState.value

        val filtered = allTasks.filter { task ->
            val matchesPriority = currentFilter.selectedPriority == null || task.priority == currentFilter.selectedPriority
            val matchesCompleted = when (currentFilter.showCompleted) {
                null -> true
                true -> task.completed
                false -> !task.completed
            }
            matchesPriority && matchesCompleted
        }

        _uiState.update { it.copy(tasks = filtered) }
    }

    fun setPriorityFilter(priority: Priority?) {
        _uiState.update { it.copy(selectedPriority = priority) }
        refreshTasks()
    }

    fun setShowCompletedFilter(showCompleted: Boolean?) {
        _uiState.update { it.copy(showCompleted = showCompleted) }
        refreshTasks()
    }

    fun toggleTaskCompletion(task: Task) {
        val updatedTask = task.copy(completed = !task.completed)
        repository.updateTask(updatedTask)
        refreshTasks()
    }

    fun deleteTask(id: Int) {
        repository.deleteTask(id)
        refreshTasks()
    }

    fun onTitleChange(value: String) {
        titleInput = value
        formError = null
    }

    fun onDescriptionChange(value: String) {
        descriptionInput = value
    }

    fun onPriorityChange(value: Priority) {
        priorityInput = value
    }

    fun onCompletedChange(value: Boolean) {
        completedInput = value
    }

    fun loadTask(id: Int?) {
        formError = null
        if (id == null) {
            // New
            isEditMode = false
            taskIdEditing = null
            titleInput = ""
            descriptionInput = ""
            priorityInput = Priority.MEDIUM
            completedInput = false
        } else {
            // Edit
            val task = repository.getTaskById(id)
            if (task != null) {
                isEditMode = true
                taskIdEditing = id
                titleInput = task.title
                descriptionInput = task.description
                priorityInput = task.priority
                completedInput = task.completed
            } else {
                loadTask(null)
            }
        }
    }

    fun saveTask(): Boolean {
        if (titleInput.isBlank()) {
            formError = "El título no puede estar vacío"
            return false
        }

        if (isEditMode) {
            val id = taskIdEditing ?: return false
            val updated = Task(
                id = id,
                title = titleInput.trim(),
                description = descriptionInput.trim(),
                priority = priorityInput,
                completed = completedInput
            )
            repository.updateTask(updated)
        } else {
            val nextId = (repository.getTasks().maxOfOrNull { it.id } ?: 0) + 1
            val newTask = Task(
                id = nextId,
                title = titleInput.trim(),
                description = descriptionInput.trim(),
                priority = priorityInput,
                completed = false
            )
            repository.addTask(newTask)
        }

        refreshTasks()
        return true
    }
}
