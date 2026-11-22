package com.dualtrack.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dualtrack.app.R
import com.dualtrack.app.data.Mode
import com.dualtrack.app.data.Task
import com.dualtrack.app.ui.components.TaskCard
import com.dualtrack.app.ui.theme.FounderSeed
import com.dualtrack.app.ui.theme.StudentSeed
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DualTrackApp(vm: DualTrackViewModel) {
    val tasks by vm.tasks.collectAsState(initial = emptyList())
    val selectedMode by vm.mode.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Text(
                            text = stringResource(R.string.timeline_title),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_task),
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            ModeSwitcher(
                selected = selectedMode,
                onSelect = { vm.setMode(it) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )

            val grouped = remember(tasks) {
                tasks.groupBy { it.dueDate }.toSortedMap()
            }

            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.empty_state),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 96.dp, top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    grouped.forEach { (epochDay, dayTasks) ->
                        item(key = "header_$epochDay") {
                            DateHeader(epochDay = epochDay)
                        }
                        items(items = dayTasks, key = { it.id }) { task ->
                            TaskCard(
                                task = task,
                                onToggle = { vm.toggle(task) },
                                onDelete = { vm.delete(task) },
                            )
                        }
                    }
                }
            }
        }
    }

    AnimatedVisibility(visible = showAddDialog, enter = fadeIn(), exit = fadeOut()) {
        AddTaskDialog(
            defaultMode = selectedMode ?: Mode.FOUNDER,
            onDismiss = { showAddDialog = false },
            onSave = { title, note, mode, date ->
                vm.addTask(title, note, mode, date)
                showAddDialog = false
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModeSwitcher(
    selected: Mode?,
    onSelect: (Mode?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = listOf(null, Mode.FOUNDER, Mode.STUDENT)
    val labels = listOf(
        stringResource(R.string.all_modes),
        stringResource(R.string.mode_founder),
        stringResource(R.string.mode_student),
    )

    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                selected = selected == option,
                onClick = { onSelect(option) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size,
                ),
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = when (option) {
                        Mode.FOUNDER -> FounderSeed
                        Mode.STUDENT -> StudentSeed
                        else -> MaterialTheme.colorScheme.primary
                    },
                ),
            ) {
                Text(labels[index], style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun DateHeader(epochDay: Long) {
    val date = LocalDate.ofEpochDay(epochDay)
    val today = LocalDate.now()
    val label = when (date) {
        today -> stringResource(R.string.today)
        today.minusDays(1) -> "Yesterday"
        today.plusDays(1) -> "Tomorrow"
        else -> date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    }
    Text(
        text = label,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTaskDialog(
    defaultMode: Mode,
    onDismiss: () -> Unit,
    onSave: (String, String, Mode, LocalDate) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf(defaultMode) }
    var showDatePicker by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf(LocalDate.now()) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    showDatePicker = false
                }) { Text(stringResource(R.string.save)) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_task)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.task_title)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text(stringResource(R.string.task_note)) },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = mode == Mode.FOUNDER,
                        onClick = { mode = Mode.FOUNDER },
                        label = { Text(stringResource(R.string.mode_founder)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = FounderSeed,
                            selectedLabelColor = androidx.compose.ui.graphics.Color.White,
                        ),
                    )
                    FilterChip(
                        selected = mode == Mode.STUDENT,
                        onClick = { mode = Mode.STUDENT },
                        label = { Text(stringResource(R.string.mode_student)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StudentSeed,
                            selectedLabelColor = androidx.compose.ui.graphics.Color.White,
                        ),
                    )
                }
                TextButton(onClick = { showDatePicker = true }) {
                    Text("${stringResource(R.string.task_date)}: ${date.format(DateTimeFormatter.ISO_LOCAL_DATE)}")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) onSave(title, note, mode, date)
                },
                enabled = title.isNotBlank(),
            ) { Text(stringResource(R.string.save)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        },
    )
}
