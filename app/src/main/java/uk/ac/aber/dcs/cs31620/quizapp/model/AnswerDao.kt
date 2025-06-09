package uk.ac.aber.dcs.cs31620.quizapp.model

import androidx.lifecycle.LiveData
import androidx.room.*
/**
 * Defines the DAO for inserting, deleting and updating Answer records as well
 * as well as getting all answers that belong to a certain question
 * @author Callum Hodges
 */
@Dao
interface AnswerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answer: Answer)

    @Query("SELECT * FROM answers WHERE questionId = :questionId")
    fun getAnswersForQuestion(questionId: Int): LiveData<List<Answer>>

    @Update
    suspend fun updateAnswer(answer: Answer)

    @Delete
    suspend fun deleteAnswer(answer: Answer)

}
