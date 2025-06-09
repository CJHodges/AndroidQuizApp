package uk.ac.aber.dcs.cs31620.quizapp.datasource

import uk.ac.aber.dcs.cs31620.quizapp.model.AnswerDao
import uk.ac.aber.dcs.cs31620.quizapp.model.QuizDao
import uk.ac.aber.dcs.cs31620.quizapp.model.QuestionDao

interface RoomDatabaseI {
    fun quizDao(): QuizDao
    fun questionDao(): QuestionDao
    fun answerDao(): AnswerDao
    fun closeDb()
}