package com.example.edulearn.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        User::class,
        Subject::class,
        Lesson::class,
        Question::class,
        Progress::class
    ],
    version = 3,
    exportSchema = false
)
abstract class EduLearnDatabase : RoomDatabase() {

    abstract fun eduLearnDao(): EduLearnDao

    companion object {

        @Volatile
        private var INSTANCE: EduLearnDatabase? = null

        fun getDatabase(context: Context): EduLearnDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EduLearnDatabase::class.java,
                    "edulearn_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}