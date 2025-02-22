package com.tomtruyen.feature.workouts.shared.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.designsystem.theme.FitnessApplicationTheme
import com.tomtruyen.core.ui.Avatar
import com.tomtruyen.data.entities.Exercise

@Composable
fun WorkoutExerciseHeader(
    exercise: Exercise,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        modifier = modifier
    ) {
        Avatar(
            imageUrl = exercise.image,
            contentDescription = exercise.displayName,
        )

        Text(
            text = exercise.displayName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = onActionClick,
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
            )
        }
    }
}

@Preview(widthDp = 300, backgroundColor = 0xFFFFFFFF)
@Composable
fun WorkoutExerciseHeaderPreview() {
    FitnessApplicationTheme {
        WorkoutExerciseHeader(
            exercise = Exercise(
                id = "123",
                name = "Bench Press",
                category = "Chest",
                equipment = "Barbell",
                image = "https://storage.googleapis.com/fitness-application-f6cf1.appspot.com/Exercises%2Fabwheel.png"
            ),
            onActionClick = {}
        )
    }
}