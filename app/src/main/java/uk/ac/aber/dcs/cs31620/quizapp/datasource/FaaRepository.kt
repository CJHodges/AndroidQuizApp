package uk.ac.aber.dcs.cs31620.quizapp.datasource

import android.app.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.ac.aber.dcs.cs31620.quizapp.model.Answer
import uk.ac.aber.dcs.cs31620.quizapp.model.Question
import uk.ac.aber.dcs.cs31620.quizapp.model.Quiz

/**
 * Provides a facade between the UI and the DAO's
 * @author Callum Hodges
 */

class FaaRepository(application: Application) {
    private val quizDao = Injection.getDatabase(application).quizDao()
    private val questionDao = Injection.getDatabase(application).questionDao()
    private val answerDao = Injection.getDatabase(application).answerDao()

    // Quiz operations
    suspend fun insertQuiz(quiz: Quiz) {
        quizDao.insertQuiz(quiz)
    }

    fun getAllQuizzes() = quizDao.getAllQuizzes()

    fun getQuizById(quizId: Int) = quizDao.getQuizById(quizId)

    suspend fun insertQuestion(question: Question): Long {
        return withContext(Dispatchers.IO) {
            questionDao.insertQuestion(question)
        }
    }

    fun getQuestionsForQuiz(quizId: Int) = questionDao.getQuestionsForQuiz(quizId)

    fun getQuestionById(questionId: Int) = questionDao.getQuestionById(questionId)

    suspend fun updateQuestion(question: Question) {
        questionDao.updateQuestion(question)
    }

    suspend fun deleteQuestion(question: Question) {
        questionDao.deleteQuestion(question)
    }

    // Answer operations
    suspend fun insertAnswer(answer: Answer) {
        answerDao.insertAnswer(answer)
    }

    suspend fun updateAnswer(answer: Answer) {
        answerDao.updateAnswer(answer)
    }

    suspend fun deleteAnswer(answer: Answer) {
        answerDao.deleteAnswer(answer) }

    fun getAnswersForQuestion(questionId: Int) = answerDao.getAnswersForQuestion(questionId)

    suspend fun clearDatabase() {
        quizDao.clearAnswers()
        quizDao.clearQuestions()
        quizDao.clearQuizzes()
    }
}
