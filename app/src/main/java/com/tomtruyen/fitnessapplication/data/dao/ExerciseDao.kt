package com.tomtruyen.fitnessapplication.data.dao

import androidx.room.Dao
import com.tomtruyen.fitnessapplication.base.BaseDao
import com.tomtruyen.fitnessapplication.data.entities.Exercise

@Dao
abstract class ExerciseDao: BaseDao<Exercise>(Exercise.TABLE_NAME) {
}