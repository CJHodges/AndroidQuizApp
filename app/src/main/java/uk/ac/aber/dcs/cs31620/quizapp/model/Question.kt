package uk.ac.aber.dcs.cs31620.quizapp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Represents a question entity in the app.
 *
 * The class defines the structure of a question object, each question has
 * an ID, the quiz ID the question belongs to, and the question text itself.
 * A question can only belong to 1 quiz, but a quiz can have many questions with
 * this.
 *
 * @property id The unique identifier for the question, generated automatically.
 * @property quizId The ID of the quiz to which this question belongs.
 * @property questionText The text of the question.
 * @author Callum Hodges
 */
@Entity(
    //quizId is set as a foreign key and linked to the id column in Quiz database.
    tableName = "questions",
    foreignKeys = [
        ForeignKey(
            entity = Quiz::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("quizId"),
            onDelete = ForeignKey.CASCADE // If a quiz is deleted, its questions are also deleted
        )
    ]
)
data class Question(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0, // ID auto generates
    var quizId: Int, // ID of the quiz this question belongs to
    var questionText: String // Text of the question
)
