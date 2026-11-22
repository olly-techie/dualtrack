package com.dualtrack.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dualtrack.app.data.Mode
import com.dualtrack.app.data.Task
import com.dualtrack.app.ui.theme.FounderContainer
import com.dualtrack.app.ui.theme.FounderOnContainer
import com.dualtrack.app.ui.theme.FounderSeed
import com.dualtrack.app.ui.theme.StudentContainer
import com.dualtrack.app.ui.theme.StudentOnContainer
import com.dualtrack.app.ui.theme.StudentSeed

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun TaskCard(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isFounder = task.mode == Mode.FOUNDER
    val accent: Color = if (isFounder) FounderSeed else StudentSeed
    val chipBg: Color = if (isFounder) FounderContainer else StudentContainer
    val chipFg: Color = if (isFounder) FounderOnContainer else StudentOnContainer

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = onDelete,
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top,
        ) {
            // Mode rail: a colored dot bar so Founder/Student are distinguishable at a glance.
            Spacer(
                modifier = Modifier
                    .width(4.dp)
                    .height(42.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        animateColorAsState(
                            targetValue = if (task.completed) Color.Gray else accent,
                            label = "accent",
                        ).value,
                    ),
            )
            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (task.completed) TextDecoration.LineThrough else null,
                    color = if (task.completed) MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.onSurface,
                )
                if (task.note.isNotBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = task.note,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(
                        modifier = Modifier
                            .size(width = 8.dp, height = 8.dp)
                            .clip(CircleShape)
                            .background(accent),
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = task.mode.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = chipFg,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(chipBg)
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                    )
                }
            }

            Spacer(Modifier.width(8.dp))
            Checkbox(
                checked = task.completed,
                onCheckedChange = { onToggle() },
                modifier = Modifier.size(28.dp),
            )
        }
    }
}

/** Small helper used when a completed item needs a checkmark badge. */
@Composable
fun DoneBadge() {
    Icon(
        imageVector = Icons.Default.Check,
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier
            .size(14.dp)
            .clip(CircleShape)
            .background(Color.Gray)
            .padding(2.dp),
    )
}
