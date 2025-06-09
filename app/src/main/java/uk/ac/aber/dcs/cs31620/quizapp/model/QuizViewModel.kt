package uk.ac.aber.dcs.cs31620.quizapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import uk.ac.aber.dcs.cs31620.quizapp.datasource.FaaRepository

// ViewModel for managing quiz-related data and operations
class QuizViewModel(application: Application) : AndroidViewModel(application) {
    // Repository to handle data operations
    private val repository: FaaRepository = FaaRepository(application)

    // LiveData for observing the list of quizzes
    var quizList: LiveData<List<Quiz>> = repository.getAllQuizzes()
        private set

    // Function to insert a new quiz into the database. Added but never implemented anywhere.
    fun insertQuiz(newQuiz: Quiz) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertQuiz(newQuiz)
        }
    }

    // Function to get a quiz by its ID
    fun getQuizById(quizId: Int): LiveData<Quiz> {
        return repository.getQuizById(quizId)
    }

    // Function to insert a new question into the database
    fun insertQuestion(newQuestion: Question): Long {
        return runBlocking {
            repository.insertQuestion(newQuestion)
        }
    }

    // Function to get the list of questions for a specific quiz
    fun getQuestionsForQuiz(quizId: Int): LiveData<List<Question>> {
        return repository.getQuestionsForQuiz(quizId)
    }

    // Function to get a question by its ID
    fun getQuestionById(questionId: Int): LiveData<Question> {
        return repository.getQuestionById(questionId)
    }

    // Function to update an existing question in the database
    fun updateQuestion(question: Question) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateQuestion(question)
        }
    }

    // Function to delete a question from the database
    fun deleteQuestion(question: Question) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteQuestion(question)
        }
    }

    // Function to insert a new answer into the database
    fun insertAnswer(newAnswer: Answer) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAnswer(newAnswer)
        }
    }

    // Function to get the list of answers for a specific question
    fun getAnswersForQuestion(questionId: Int): LiveData<List<Answer>> {
        return repository.getAnswersForQuestion(questionId)
    }

    // Function to update an existing answer in the database
    fun updateAnswer(answer: Answer) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAnswer(answer)
        }
    }

    // Function to delete an answer from the database
    fun deleteAnswer(answer: Answer) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAnswer(answer)
        }
    }

    // Function to clear the entire database. Never Implemented or planned to, just added as a precaution.
    fun clearDatabase() {
        viewModelScope.launch {
            repository.clearDatabase()
        }
    }
}
