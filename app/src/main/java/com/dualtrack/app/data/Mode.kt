package com.dualtrack.app.data

/**
 * The two "brains" DualTrack keeps separate.
 */
enum class Mode(val label: String) {
    FOUNDER("Founder"),
    STUDENT("Student");

    companion object {
        fun from(value: String): Mode = entries.firstOrNull { it.name == value } ?: FOUNDER
    }
}
