package com.example.edulearn.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val lessonId: Int,

    val question: String,

    val optionA: String,

    val optionB: String,

    val optionC: String,

    val optionD: String,

    val correctAnswer: String
)