package com.dualtrack.app.data

import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth for task data. The UI never talks to the DAO directly.
 */
class TaskRepository(private val dao: TaskDao) {

    fun observeAll(): Flow<List<Task>> = dao.observeAll()

    fun observeByMode(mode: Mode): Flow<List<Task>> = dao.observeByMode(mode)

    suspend fun add(title: String, note: String, mode: Mode, dueDate: Long) {
        dao.insert(
            Task(title = title.trim(), note = note.trim(), mode = mode, dueDate = dueDate)
        )
    }

    suspend fun toggle(task: Task) = dao.update(task.copy(completed = !task.completed))

    suspend fun delete(task: Task) = dao.delete(task)

    suspend fun clearCompleted() = dao.deleteCompleted()
}
