package uk.ac.aber.dcs.cs31620.quizapp.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Defines the DAO for inserting Quiz records as well
 * as getting all quizzes in alphabetical order, based off
 * their name/title. The DAO also can clear any of the 3 databases.
 * @author Callum Hodges
 */

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: Quiz)

    @Query("SELECT * FROM quizzes ORDER BY name ASC")
    fun getAllQuizzes(): LiveData<List<Quiz>>

    @Query("SELECT * FROM quizzes WHERE id = :quizId")
    fun getQuizById(quizId: Int): LiveData<Quiz>

    @Query("DELETE FROM quizzes")
    suspend fun clearQuizzes()

    @Query("DELETE FROM questions")
    suspend fun clearQuestions()

    @Query("DELETE FROM answers")
    suspend fun clearAnswers()



}



