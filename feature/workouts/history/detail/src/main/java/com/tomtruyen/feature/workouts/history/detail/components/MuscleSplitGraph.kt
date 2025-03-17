package com.tomtruyen.feature.workouts.history.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.common.extensions.rounded
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.data.models.ui.WorkoutHistoryExerciseUiModel

@Composable
fun MuscleSplitGraph(
    exercises: List<WorkoutHistoryExerciseUiModel>
) {
    val muscleGroups = remember(exercises) {
        exercises.getMuscleSplitPercentages()
    }

    Column(
        modifier = Modifier.padding(
            horizontal = Dimens.Normal
        ),
        verticalArrangement = Arrangement.spacedBy(Dimens.Small)
    ) {
        muscleGroups.forEach { (group, percentage) ->
            MuscleSplitItem(
                group = group,
                percentage = percentage
            )
        }
    }
}

@Composable
private fun MuscleSplitItem(
    group: String,
    percentage: Double
) {
    val fractionPercentage = remember(percentage) {
        percentage.toFloat() / 100
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = "$group (${percentage.rounded(0)}%)",
            style = MaterialTheme.typography.bodySmall
        )

        Box(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth(fractionPercentage)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(Dimens.Tiny)
                )
        )
    }
}

private fun List<WorkoutHistoryExerciseUiModel>.getMuscleSplitPercentages(): Map<String, Double> {
    if (isEmpty()) return emptyMap()

    val exercises = this.filter { it.category != null }

    val groupedCategories = exercises.groupingBy { it.category!! }.eachCount()

    val totalExercises = groupedCategories.values.sum().toDouble()

    return groupedCategories.mapValues { (_, count) ->
        (count / totalExercises) * 100
    }
}