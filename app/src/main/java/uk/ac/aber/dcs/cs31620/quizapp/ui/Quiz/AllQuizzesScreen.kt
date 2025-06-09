package uk.ac.aber.dcs.cs31620.quizapp.ui.Quiz

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.compose.QuizAppTheme
import uk.ac.aber.dcs.cs31620.quizapp.ui.components.QuizCard
import uk.ac.aber.dcs.cs31620.quizapp.model.Quiz
import uk.ac.aber.dcs.cs31620.quizapp.model.QuizViewModel
import uk.ac.aber.dcs.cs31620.quizapp.ui.components.DefaultSnackbar
import uk.ac.aber.dcs.cs31620.quizapp.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.quizapp.ui.navigation.Screen

/**
 * Composable function to display the top-level screen for all quizzes.
 * This code was based on the code that Chris had in his CatScreen.kt file,
 * but has been changed/heavily amended
 *
 * @param navController Navigation controller to handle navigation actions.
 * @param quizzesViewModel ViewModel to handle the quiz data and logic
 * @author Callum Hodges
 */
@Composable
fun AllQuizzesScreenTopLevel(
    navController: NavHostController,
    quizzesViewModel: QuizViewModel = viewModel()
) {
    // Get the quiz list using ViewModel
    val quizList by quizzesViewModel.quizList.observeAsState(listOf())
    AllQuizzes(
        quizzesList = quizList,
        navController = navController
    )
}

/**
 * Composable function to display the list of all quizzes as quiz cards.
 *
 * @param quizzesList List of quizzes to display.
 * @param navController Navigation controller to handle navigation actions.
 * @author Callum Hodges
 */
@Composable
fun AllQuizzes(
    quizzesList: List<Quiz> = listOf(),
    navController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    TopLevelScaffold(
        navController = navController,
        snackbarContent = { data ->
            DefaultSnackbar(
                data = data,
                modifier = Modifier.padding(bottom = 4.dp),
                onDismiss = { data.dismiss() }
            )
        },
        coroutineScope = coroutineScope,
        snackbarHostState = snackbarHostState,
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Handle Floating action button when clicked */ }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Quiz")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            val context = LocalContext.current

            // LazyColumn to display the list of quiz cards
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            ) {
                items(quizzesList) { quiz ->
                    QuizCard(
                        quiz = quiz,
                        navController = navController,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .fillMaxWidth(),
                        selectAction = { selectedQuiz ->
                            // Navigate to the PlayQuiz screen with the relevant quiz ID
                            navController.navigate("${Screen.PlayQuiz.route}/${quiz.id}") {
                                launchSingleTop = true
                            }
                        },
                        editAction = { editedQuiz ->
                            Log.d("QuizNavigation", "Navigating to EditQuiz with ID: ${quiz.id}")
                            // Navigate to the EditQuiz screen with the relevant quiz ID
                            navController.navigate("${Screen.EditQuiz.route}/${quiz.id}") {
                                launchSingleTop = true
                            }
                        },
                        deleteAction = { deletedQuiz ->
                            // Display a Toast message and log the delete action
                            Toast.makeText(
                                context, "Delete ${deletedQuiz.name}",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.d("QuizNavigation", "Navigating to EditQuiz with ID: ${quiz.id}")
                        }
                    )
                }
            }
        }
    }
}

/**
 * Preview function for the AllQuizzes screen.
 */
@Preview
@Composable
fun AllQuizzesPreview() {
    val navController = rememberNavController()
    QuizAppTheme(dynamicColor = false) {
        AllQuizzes(navController = navController)
    }
}
