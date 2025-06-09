package uk.ac.aber.dcs.cs31620.quizapp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Represents an answer entity in the app.
 *
 * The class defines the structure of an answer object, each answer has
 * an ID, question ID it belongs to, answer text, and a boolean flag indicating
 * if it is the correct answer. This Set-up allows for an answer to belong to only 1
 * question, but allows for a question to have many answers.
 *
 * @property id The unique identifier for the answer, generated automatically.
 * @property questionId The ID of the question to which this answer belongs.
 * @property answerText The text of the answer.
 * @property isCorrect Flag to indicate if this answer is correct.
 * @author Callum Hodges
 */
@Entity(
    //questionId is set as a foreign key and linked to the id column in question database.
    tableName = "answers",
    foreignKeys = [
        ForeignKey(
            entity = Question::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("questionId"),
            onDelete = ForeignKey.CASCADE // If a question is deleted, its answers are also deleted
        )
    ]
)
data class Answer(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0, // ID auto generates
    var questionId: Int, // ID of the question this answer belongs to
    var answerText: String, // Text of the answer
    var isCorrect: Boolean // Boolean Flag indicating if this is the correct answer
)
