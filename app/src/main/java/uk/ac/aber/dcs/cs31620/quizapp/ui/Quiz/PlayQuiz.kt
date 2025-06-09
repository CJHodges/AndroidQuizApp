package uk.ac.aber.dcs.cs31620.quizapp.ui.playQuiz

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.quizapp.model.QuizViewModel
import uk.ac.aber.dcs.cs31620.quizapp.ui.navigation.Screen


/**
 * Composable function to display the top-level screen for playing a quiz.
 * Current Implementation finds all the quiz question Id's related to the quiz
 * and shuffles them before the quiz starts.
 * @param navController Navigation controller to handle navigation actions.
 * @param quizId ID of the quiz to be played.
 * @param quizzesViewModel ViewModel to handle quiz data and logic
 * @author Callum Hodges
 */
@Composable
fun PlayQuizScreenTopLevel(
    navController: NavHostController,
    quizId: Int,
    quizzesViewModel: QuizViewModel = viewModel()
) {

    //Observing the questions for the specified quiz from the ViewModel.
    val questions by quizzesViewModel.getQuestionsForQuiz(quizId).observeAsState(listOf())

     // Remembering the shuffled list of question IDs to randomize the order of questions.
    val shuffledQuestionIds = remember(questions) { questions.map { it.id }.shuffled() }


    // Log debugging to check the quizId and question list.
    Log.d("PlayQuizScreenTopLevel", "Quiz ID: $quizId, Questions: $questions")

    //Composable function call to display the quiz screen with its provided parameters.
    PlayQuizScreen(
        navController = navController,
        questionIds = shuffledQuestionIds,
        quizzesViewModel = quizzesViewModel,
        quizId = quizId
    )
}

/**
 * Composable function to display the quiz screen.
 * Currently the quiz iterates through the shuffled question ID list and
 * then uses the ID to find the question text, and answers, these are then
 * displayed on screen.
 * @param navController Navigation controller to handle navigation actions.
 * @param questionIds List of question IDs to be displayed in the quiz.
 * @param quizzesViewModel ViewModel to handle quiz data and logic.
 * @param quizId ID of the quiz to be played.
 * @author Callum Hodges

 */
@Composable
fun PlayQuizScreen(
    navController: NavHostController,
    questionIds: List<Int>,
    quizzesViewModel: QuizViewModel,
    quizId: Int
) {
    // The variables used to keep track of the current question, selected answer, correct answers, and quiz completion status
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var correctAnswers by remember { mutableStateOf(0) }
    var quizCompleted by remember { mutableStateOf(false) }

    // Log the questionIds and current state
    Log.d(
        "PlayQuizScreen",
        "Question IDs: $questionIds, Current Question Index: $currentQuestionIndex"
    )

    if (quizCompleted) {
        // Display the quiz completion screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Quiz Completed",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)
            )

            Text(
                text = "Your Score: $correctAnswers / ${questionIds.size}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                //Sends the user back to this page, this is done to shuffle the question order again on restart.
                Button(
                    onClick = {
                        navController.navigate("${Screen.PlayQuiz.route}/${quizId}") {
                            launchSingleTop = true
                        }

                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .height(70.dp)
                        .width(160.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.RestartAlt,
                        contentDescription = "Restart Quiz",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Restart Quiz", fontSize = 18.sp)
                }
                //This button will send the user back to the all quizzes screen.
                Button(
                    onClick = {
                        navController.navigate(Screen.AllQuizzes.route)
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .height(70.dp)
                        .width(160.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Home,
                        contentDescription = "Home",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Home", fontSize = 18.sp)
                }
            }
        }
    } else {
        //If quiz is not finished and the current question is not the last one continue
        if (currentQuestionIndex < questionIds.size) {
            //Get the current question and its answers
            val currentQuestionId = questionIds[currentQuestionIndex]
            val currentQuestion by quizzesViewModel.getQuestionById(currentQuestionId)
                .observeAsState()
            val answers by quizzesViewModel.getAnswersForQuestion(currentQuestionId)
                .observeAsState(listOf())

            currentQuestion?.let { question ->
                //Display the current question and its answers
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = question.questionText,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${currentQuestionIndex + 1} / ${questionIds.size}",
                            fontSize = 18.sp
                        )
                    }

                    // Display the multiple-choice answers
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(answers) { answer ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = answer.answerText == selectedAnswer,
                                    onClick = { selectedAnswer = answer.answerText }
                                )
                                Text(
                                    text = answer.answerText,
                                    modifier = Modifier.padding(start = 8.dp),
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Navigation buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    ) {
                        Button(
                            onClick = {
                                navController.navigateUp()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                                .height(70.dp)
                                .width(160.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home"
                            )
                            Text(text = "Home", fontSize = 18.sp)
                        }

                        //Button logic to check if the selected answer is correct and the logic to increment the score
                        Button(
                            onClick = {
                                //find the correct answer in the list and compare the answer text to the users selected answer text
                                if (selectedAnswer == answers.find { it.isCorrect }?.answerText) {
                                    correctAnswers++
                                }
                                //If the quiz still has questions increment question index and reset the selected answer
                                if (currentQuestionIndex < questionIds.size - 1) {
                                    currentQuestionIndex++
                                    selectedAnswer = null
                                } else {
                                    quizCompleted = true
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(70.dp)
                                .width(160.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Next Question"
                            )
                            Text(text = "Next Question", fontSize = 18.sp)
                        }
                    }
                }
            } ?: run {
                // Handle the case where the question is not yet available
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else {
            //If the quiz has no questions Display this screen
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No questions available for this quiz.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            navController.navigate(Screen.AllQuizzes.route)
                        },
                        modifier = Modifier
                            .height(70.dp)
                            .width(160.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Home,
                            contentDescription = "Home",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Home", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}
