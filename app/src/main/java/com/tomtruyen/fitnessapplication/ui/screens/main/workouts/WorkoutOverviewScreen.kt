package com.tomtruyen.fitnessapplication.ui.screens.main.workouts

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph
@Destination
@Composable
fun WorkoutOverviewScreen() {
    WorkoutOverviewScreenLayout()
}

@Composable
fun WorkoutOverviewScreenLayout() {
    Text(text = "WorkoutOverviewScreen")
}
