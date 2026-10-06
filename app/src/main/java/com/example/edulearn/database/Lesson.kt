package com.example.edulearn.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lessons")
data class Lesson(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val subjectId: Int,

    val title: String,

    val content: String
)