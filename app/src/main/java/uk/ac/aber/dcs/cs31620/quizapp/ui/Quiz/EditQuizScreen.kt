package uk.ac.aber.dcs.cs31620.quizapp.ui.editQuiz

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.compose.QuizAppTheme
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.quizapp.R
import uk.ac.aber.dcs.cs31620.quizapp.model.Answer
import uk.ac.aber.dcs.cs31620.quizapp.model.Question
import uk.ac.aber.dcs.cs31620.quizapp.model.Quiz
import uk.ac.aber.dcs.cs31620.quizapp.model.QuizViewModel
import uk.ac.aber.dcs.cs31620.quizapp.ui.navigation.Screen

/**
 * Composable function to display the top-level screen for editing a quiz.
 *
 * @param navController Navigation controller to handle navigations.
 * @param quizId ID of the quiz to be edited.
 * @param quizzesViewModel ViewModel to handle quiz data and logic
 * @author Callum Hodges
 */
@Composable
fun EditQuizScreenTopLevel(
    navController: NavHostController,
    quizId: Int,
    quizzesViewModel: QuizViewModel = viewModel()
) {
    // get the quiz and its questions from the ViewModel
    val quiz by quizzesViewModel.getQuizById(quizId).observeAsState()
    val questions by quizzesViewModel.getQuestionsForQuiz(quizId).observeAsState(listOf())

    // If the quiz exists, display the EditQuizScreen
    quiz?.let {
        EditQuizScreen(
            navController = navController,
            quiz = it,
            questions = questions,
            quizzesViewModel = quizzesViewModel
        )
    }
}

/**
 * Composable function to display the screen for editing a quiz and its questions.
 *
 * @param navController Navigation controller to handle navigations.
 * @param quiz The quiz to be edited.
 * @param questions The list of questions associated with the quiz.
 * @param quizzesViewModel ViewModel to handle quiz data and logic.
 * @author Callum Hodges
 */
@Composable
fun EditQuizScreen(
    navController: NavHostController,
    quiz: Quiz,
    questions: List<Question>,
    quizzesViewModel: QuizViewModel = viewModel()
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var quizQuestions by remember { mutableStateOf(questions) }

    // Observer for live data snapshot changes
    val updatedQuestions by quizzesViewModel.getQuestionsForQuiz(quiz.id)
        .observeAsState(initial = emptyList())

    // Update the quizQuestions whenever there's a change in the live data snapshot
    LaunchedEffect(updatedQuestions) {
        quizQuestions = updatedQuestions
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Back button to navigate back
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.back)
            )
        }

        // Display the quiz name and description
        Text(
            text = quiz.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = quiz.description,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // LazyColumn to display the list of questions
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp)
        ) {
            //create an index number for questions that auto updates when a question is deleted
            itemsIndexed(quizQuestions) { index, question ->
                QuestionCard(
                    questionNumber = index + 1,
                    question = question,
                    onClick = {
                        navController.navigate("${Screen.EditQuestion.route}/${question.id}")
                    },
                    onDelete = {
                        coroutineScope.launch {
                            quizzesViewModel.deleteQuestion(question)
                            quizQuestions = quizQuestions.toMutableList().apply { removeAt(index) }
                            Toast.makeText(context, "Question deleted", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }

        /* When new question button is pressed, create a question linked
        to the quiz, with a temporary name and 3 temporary answers. And immediately
        add this to the database. Has some upsides and downsides doing this*/
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        val newQuestion =
                            Question(quizId = quiz.id, questionText = "New Question")
                        Log.d("QuizApp", "Creating new question: $newQuestion")

                        val questionId = quizzesViewModel.insertQuestion(newQuestion)
                        Log.d("QuizApp", "Inserted question ID: $questionId")
                        val newAnswers = listOf(
                            Answer(
                                questionId = questionId.toInt(),
                                answerText = "a",
                                isCorrect = true
                            ),
                            Answer(
                                questionId = questionId.toInt(),
                                answerText = "b",
                                isCorrect = false
                            ),
                            Answer(
                                questionId = questionId.toInt(),
                                answerText = "c",
                                isCorrect = false
                            )
                        )
                        newAnswers.forEach { answer ->
                            Log.d("QuizApp", "Inserting answer: $answer")
                            quizzesViewModel.insertAnswer(answer)
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(65.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_question)
                )
                Text(
                    text = stringResource(id = R.string.add_question),
                    fontSize = 18.sp
                )
            }
        }
    }
}

/**
 * Composable function to display a question card.
 *
 * @param questionNumber The number of the question.
 * @param question The question to display.
 * @param onClick Action to perform when the card is clicked.
 * @param onDelete Action to perform when the delete button is clicked.
 * @author Callum Hodges
 */
//This composable is only ever used in this screen so was not necessary to make this a separate class in components
@Composable
fun QuestionCard(
    questionNumber: Int,
    question: Question,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Question $questionNumber",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = question.questionText,
                    fontSize = 18.sp
                )
            }

            Row {
                IconButton(onClick = { onClick() }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.edit_question)
                    )
                }
                IconButton(onClick = { onDelete() }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.delete_question)
                    )
                }
            }
        }
    }
}

/**
 * Preview function for the EditQuizScreen.
 */
@Preview
@Composable
fun EditQuizScreenPreview() {
    val navController = rememberNavController()
    QuizAppTheme(dynamicColor = false) {
        EditQuizScreen(
            navController = navController,
            quiz = Quiz(
                id = 1,
                name = "Sample Quiz",
                description = "This is a sample quiz description",
                imagePath = "sample.jpg"
            ),
            questions = listOf(
                Question(id = 1, quizId = 1, questionText = "Question 1"),
                Question(id = 2, quizId = 1, questionText = "Question 2")
            )
        )
    }
}
