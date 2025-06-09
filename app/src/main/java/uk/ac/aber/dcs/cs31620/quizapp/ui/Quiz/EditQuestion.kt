package uk.ac.aber.dcs.cs31620.quizapp.ui.editQuestion

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import uk.ac.aber.dcs.cs31620.quizapp.model.QuizViewModel

/**
 * Composable function to display the top-level screen for editing a question in a quiz.
 *
 * @param navController Navigation controller to handle navigation actions.
 * @param questionId ID of the question to be edited.
 * @param quizzesViewModel ViewModel to handle quiz data and logic
 * @author Callum Hodges
 */
@Composable
fun EditQuestionScreenTopLevel(
    navController: NavHostController,
    questionId: Int,
    quizzesViewModel: QuizViewModel = viewModel()
) {
    // Observing the question and its answers from the ViewModel
    val question by quizzesViewModel.getQuestionById(questionId).observeAsState()
    val answers by quizzesViewModel.getAnswersForQuestion(questionId).observeAsState(listOf())

    // If the question exists, display the EditQuestionScreen
    question?.let {
        EditQuestionScreen(
            navController = navController,
            question = it,
            answers = answers,
            quizzesViewModel = quizzesViewModel
        )
    }
}

/**
 * Composable function to display the screen for editing a question and its answers.
 *
 * @param navController Navigation controller to handle navigation actions.
 * @param question The question to be edited.
 * @param answers The list of answers associated with the question.
 * @param quizzesViewModel ViewModel to handle quiz data and logic.
 * @author Callum Hodges
 */
@Composable
fun EditQuestionScreen(
    navController: NavHostController,
    question: Question,
    answers: List<Answer>,
    quizzesViewModel: QuizViewModel = viewModel()
) {
    // State variables to manage the current question and answers
    var questionText by remember { mutableStateOf(question.questionText) }
    var answerTexts by remember { mutableStateOf(answers.map { it.answerText }) }
    var correctAnswerIndex by remember { mutableStateOf(answers.indexOfFirst { it.isCorrect }) }
    var answersState by remember { mutableStateOf(answers.toMutableList()) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

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

        // Title for the edit question screen
        Text(
            text = stringResource(id = R.string.edit_question_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Text field for editing the question text
        TextField(
            value = questionText,
            onValueChange = { questionText = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // No immediate update to the database
                }
            ),
            label = { Text("Question Text") },
            placeholder = { Text("Enter Question here") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title for the answers section
        Text(
            text = "Answers:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // LazyColumn to display the list of answers
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            items(answersState.size) { index ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    // Radio button to select the correct answer
                    RadioButton(
                        selected = index == correctAnswerIndex,
                        onClick = { correctAnswerIndex = index }
                    )
                    // Text field for editing the answer text
                    TextField(
                        value = answerTexts[index],
                        onValueChange = { newText ->
                            answerTexts = answerTexts.toMutableList().apply {
                                this[index] = newText
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        placeholder = { Text("Enter Answer here") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    )
                    // Button to delete an answer
                    IconButton(
                        onClick = {
                            if (answersState.size > 1) {
                                answersState = answersState.toMutableList().apply { removeAt(index) }
                                answerTexts = answerTexts.toMutableList().apply { removeAt(index) }
                            } else {
                                Toast.makeText(
                                    context,
                                    "You must have at least one answer.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(id = R.string.delete_answer)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Row for adding new answers and saving the question
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Button to add a new answer, which disables after 10 answers are provided
            Button(
                onClick = {
                    if (answersState.size < 10) {
                        answersState = answersState.toMutableList().apply {
                            add(Answer(id = 0, questionId = question.id, answerText = "", isCorrect = false))
                        }
                        answerTexts = answerTexts.toMutableList().apply { add("") }
                    }
                },
                enabled = answersState.size < 10,
                modifier = Modifier.size(width = 150.dp, height = 60.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_answer)
                )
                Text(text = stringResource(id = R.string.add_answer))
            }

            // Button to save the question and answers, Saves all changes, As LONG AS the there are no blanks
            Button(
                onClick = {
                    if (questionText.isBlank()) {
                        Toast.makeText(context, "Question Text cannot be empty", Toast.LENGTH_SHORT).show()
                    } else if (answerTexts.any { it.isBlank() }) {
                        Toast.makeText(context, "All Answer fields must be filled", Toast.LENGTH_SHORT).show()
                    } else {
                        coroutineScope.launch {
                            // Update question text
                            question.questionText = questionText
                            quizzesViewModel.updateQuestion(question)

                            // Update answers
                            val updatedAnswers = answerTexts.mapIndexed { index, text ->
                                answersState.getOrNull(index)?.copy(answerText = text, isCorrect = index == correctAnswerIndex)
                                    ?: Answer(id = 0, questionId = question.id, answerText = text, isCorrect = index == correctAnswerIndex)
                            }

                            // Delete answers that were removed
                            val existingAnswerIds = answers.map { it.id }
                            val updatedAnswerIds = updatedAnswers.map { it.id }
                            val deletedAnswers = answers.filter { it.id !in updatedAnswerIds }
                            deletedAnswers.forEach { quizzesViewModel.deleteAnswer(it) }

                            // Save updated answers
                            updatedAnswers.forEach { answer ->
                                if (existingAnswerIds.contains(answer.id)) {
                                    quizzesViewModel.updateAnswer(answer)
                                } else {
                                    quizzesViewModel.insertAnswer(answer)
                                }
                            }

                            // Notify user and navigate back
                            Toast.makeText(context, "Question and answers saved", Toast.LENGTH_SHORT).show()
                            navController.navigateUp()
                        }
                    }
                },
                modifier = Modifier.size(width = 150.dp, height = 60.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = stringResource(id = R.string.save)
                )
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}



/**
 * Preview function for the EditQuestionScreen.
 */
@Preview
@Composable
fun EditQuestionScreenPreview() {
    val navController = rememberNavController()
    QuizAppTheme(dynamicColor = false) {
        EditQuestionScreen(
            navController = navController,
            question = Question(id = 1, quizId = 1, questionText = "Sample Question"),
            answers = listOf(
                Answer(
                    id = 1,
                    questionId = 1,
                    answerText = "Sample Answer 1",
                    isCorrect = false
                ),
                Answer(
                    id = 2,
                    questionId = 1,
                    answerText = "Sample Answer 2",
                    isCorrect = false
                ),
                Answer(id = 3, questionId = 1, answerText = "Sample Answer 3", isCorrect = true)
            )
        )
    }
}
