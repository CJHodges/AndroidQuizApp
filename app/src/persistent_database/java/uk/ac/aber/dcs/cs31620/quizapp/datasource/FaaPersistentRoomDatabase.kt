package uk.ac.aber.dcs.cs31620.quizapp.datasource

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.quizapp.model.Answer
import uk.ac.aber.dcs.cs31620.quizapp.model.AnswerDao
import uk.ac.aber.dcs.cs31620.quizapp.model.Question
import uk.ac.aber.dcs.cs31620.quizapp.model.QuestionDao
import uk.ac.aber.dcs.cs31620.quizapp.model.Quiz
import uk.ac.aber.dcs.cs31620.quizapp.model.QuizDao

@Database(entities = [Quiz::class, Question::class, Answer::class], version = 3, exportSchema = true)
abstract class FaaPersistentRoomDatabase : RoomDatabase(), RoomDatabaseI {
    abstract override fun quizDao(): QuizDao
    abstract override fun questionDao(): QuestionDao
    abstract override fun answerDao(): AnswerDao

    override fun closeDb() {
        instance?.close()
        instance = null
    }

    companion object {
        private var instance: FaaPersistentRoomDatabase? = null
        private val coroutineScope = CoroutineScope(Dispatchers.IO)

        @Synchronized
        fun getDatabase(context: Context): FaaPersistentRoomDatabase? {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        FaaPersistentRoomDatabase::class.java,
                        "faa_database"
                    )
                        .addCallback(roomDatabaseCallback(context))
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3) // Include all migrations
                        .build()
            }
            return instance
        }

        private fun roomDatabaseCallback(context: Context): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    coroutineScope.launch {
                        populateDatabase(context, getDatabase(context)!!)
                    }
                }
            }
        }

        suspend fun populateDatabase(context: Context, instance: FaaPersistentRoomDatabase) {
            // Create a quiz that is empty, but has some dummy data for the title and description and file path
            val pythonCodingQuiz = Quiz(
                id= 0,
                name = "Python Coding",
                description = "An introductory quiz to Python programming concepts, designed to test your knowledge and skills.",
                imagePath = "pythonimage.jpg"
            )

            // Insert the quiz into the database
            val quizDao = instance.quizDao()
            quizDao.insertQuiz(pythonCodingQuiz)
        }

        // Define the migration strategy for quizzes table
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.d("migrate", "Doing a migrate from version 1 to 2")
                // Create quizzes table with the correct schema
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `quizzes` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                    `name` TEXT NOT NULL, 
                    `description` TEXT NOT NULL, 
                    `imagePath` TEXT NOT NULL)
                    """.trimIndent()
                )
            }
        }

        // Define the migration strategy for questions and answers tables
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.d("migrate", "Doing a migrate from version 2 to 3")
                // Create questions table with the correct schema
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `questions` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                    `quizId` INTEGER NOT NULL, 
                    `questionText` TEXT NOT NULL, 
                    FOREIGN KEY(`quizId`) REFERENCES `quizzes`(`id`) ON DELETE CASCADE)
                    """.trimIndent()
                )

                // Create answers table with the correct schema
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `answers` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                    `questionId` INTEGER NOT NULL, 
                    `answerText` TEXT NOT NULL,
                    `isCorrect` INTEGER NOT NULL CHECK (isCorrect IN (0, 1)),
                    FOREIGN KEY(`questionId`) REFERENCES `questions`(`id`) ON DELETE CASCADE)
                    """.trimIndent()
                )
            }
        }
    }
}
