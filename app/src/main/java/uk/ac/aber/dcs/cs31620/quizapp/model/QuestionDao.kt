package uk.ac.aber.dcs.cs31620.quizapp.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

/**
 * Defines the DAO for inserting, deleting and updating Question records.
 * It also gets all Questions that belong to a certain Quiz, and gets
 * individual Questions using their questionID.
 * @author Callum Hodges
 */

@Dao
interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: Question): Long


    @Query("SELECT * FROM questions WHERE quizId = :quizId")
    fun getQuestionsForQuiz(quizId: Int): LiveData<List<Question>>

    @Query("SELECT * FROM questions WHERE id = :questionId")
    fun getQuestionById(questionId: Int): LiveData<Question>

    @Update
    suspend fun updateQuestion(question: Question)

    @Delete
    suspend fun deleteQuestion(question: Question)
}
