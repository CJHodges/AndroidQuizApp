package uk.ac.aber.dcs.cs31620.quizapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a quiz entity in the app.
 *
 * The class defines the structure of a quiz object, each quiz has
 * an ID unique to the quiz, a name/title for the quiz, a description
 * on what the quiz is about, and image path leading to an image for the quiz.
 *
 * @property id The unique identifier for the quiz, generated automatically.
 * @property name The Name/Title of the quiz.
 * @property description A brief description of the quiz.
 * @property imagePath The path to the image associated with the quiz.
 * @author Callum Hodges
 */
@Entity(tableName = "quizzes")
data class Quiz(
    @PrimaryKey(autoGenerate = true) // ID auto generates
    var id: Int = 0,
    var name: String = "",
    var description: String = "",
    var imagePath: String = ""
)
