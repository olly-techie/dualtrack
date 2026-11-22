package com.dualtrack.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A single item on the timeline. [dueDate] is an epoch-day (days since 1970-01-01,
 * UTC) so a task stays on the same calendar day regardless of timezone.
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val note: String = "",
    val mode: Mode,
    val dueDate: Long,
    val completed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
)
