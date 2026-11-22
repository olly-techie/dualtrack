package com.dualtrack.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dualtrack.app.data.AppDatabase
import com.dualtrack.app.data.Mode
import com.dualtrack.app.data.Task
import com.dualtrack.app.data.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class DualTrackViewModel(private val repo: TaskRepository) : ViewModel() {

    private val _mode = MutableStateFlow<Mode?>(null) // null = All
    val mode: StateFlow<Mode?> = _mode.asStateFlow()

    val tasks: Flow<List<Task>> = _mode.flatMapLatest { selected ->
        when (selected) {
            null -> repo.observeAll()
            else -> repo.observeByMode(selected)
        }
    }

    fun setMode(mode: Mode?) {
        _mode.value = mode
    }

    fun addTask(title: String, note: String, mode: Mode, date: LocalDate) {
        viewModelScope.launch {
            repo.add(title, note, mode, date.toEpochDay()) // java.time LocalDate.toEpochDay()
        }
    }

    fun toggle(task: Task) {
        viewModelScope.launch { repo.toggle(task) }
    }

    fun delete(task: Task) {
        viewModelScope.launch { repo.delete(task) }
    }

    class Factory(private val repo: TaskRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DualTrackViewModel::class.java)) {
                return DualTrackViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

/** Convenience so screens don't need to construct repositories themselves. */
fun repositoryFor(application: android.app.Application): TaskRepository =
    TaskRepository(AppDatabase.get(application).taskDao())
