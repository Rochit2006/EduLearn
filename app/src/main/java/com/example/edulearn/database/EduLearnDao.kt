package com.example.edulearn.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EduLearnDao {

    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("""
        SELECT * FROM users
        WHERE email = :email AND password = :password
        LIMIT 1
    """)
    suspend fun loginUser(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): User?

    @Update
    suspend fun updateUser(user: User)

    @Insert
    suspend fun insertSubject(subject: Subject): Long

    @Query("SELECT * FROM subjects")
    suspend fun getAllSubjects(): List<Subject>

    @Query("SELECT * FROM subjects WHERE id = :subjectId LIMIT 1")
    suspend fun getSubjectById(subjectId: Int): Subject?

    @Insert
    suspend fun insertLesson(lesson: Lesson): Long

    @Query("SELECT * FROM lessons WHERE subjectId = :subjectId")
    suspend fun getLessonsBySubject(subjectId: Int): List<Lesson>

    @Query("SELECT * FROM lessons WHERE id = :lessonId LIMIT 1")
    suspend fun getLessonById(lessonId: Int): Lesson?

    @Insert
    suspend fun insertQuestion(question: Question): Long

    @Query("SELECT * FROM questions WHERE lessonId = :lessonId")
    suspend fun getQuestionsByLesson(lessonId: Int): List<Question>

    @Insert
    suspend fun saveProgress(progress: Progress): Long

    @Query("""
        SELECT * FROM progress
        WHERE userId = :userId
        ORDER BY id DESC
    """)
    suspend fun getProgressByUser(userId: Int): List<Progress>

    @Query("""
        SELECT * FROM progress
        WHERE userId = :userId AND lessonId = :lessonId
        LIMIT 1
    """)
    suspend fun getProgressByUserAndLesson(
        userId: Int,
        lessonId: Int
    ): Progress?

    @Query("""
        DELETE FROM progress
        WHERE userId = :userId AND lessonId = :lessonId
    """)
    suspend fun deleteProgressByUserAndLesson(
        userId: Int,
        lessonId: Int
    )
}