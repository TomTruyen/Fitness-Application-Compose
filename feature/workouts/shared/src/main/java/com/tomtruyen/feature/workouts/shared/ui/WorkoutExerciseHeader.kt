package com.tomtruyen.feature.workouts.shared.ui

import android.graphics.Paint.Align
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tomtruyen.core.designsystem.theme.FitnessApplicationTheme
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.core.common.R as CommonR

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
        AsyncImage(
            model = exercise.image,
            fallback = painterResource(CommonR.drawable.ic_fallback),
            contentDescription = exercise.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(40.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
        )

        Text(
            text = exercise.name.orEmpty(),
            style = MaterialTheme.typography.bodyLarge
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