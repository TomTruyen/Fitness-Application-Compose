package com.tomtruyen.fitnessapplication.navigation

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph
@NavGraph
annotation class ExercisesNavGraph(
    val start: Boolean = false
)

@RootNavGraph
@NavGraph
annotation class CreateWorkoutNavGraph(
    val start: Boolean = false
)