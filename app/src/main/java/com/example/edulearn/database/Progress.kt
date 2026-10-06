package com.example.edulearn.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progress")
data class Progress(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: Int,

    val subjectId: Int,

    val lessonId: Int,

    val completed: Boolean = false,

    val score: Int = 0
)