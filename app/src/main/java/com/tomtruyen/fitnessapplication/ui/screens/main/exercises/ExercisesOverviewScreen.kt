package com.tomtruyen.fitnessapplication.ui.screens.main.exercises

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph
@Destination
@Composable
fun ExercisesScreen() {
    ExercisesScreenLayout()
}

@Composable
fun ExercisesScreenLayout() {
    Text(text = "ExercisesScreen")
}