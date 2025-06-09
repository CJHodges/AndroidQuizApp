package uk.ac.aber.dcs.cs31620.quizapp.ui.Favourite

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
 * but has been changed/heavily amended. This is supposed to be functionally identical to
 * AllQuizzes, but instead of grabbing all quiz data it was supposed to only supposed get
 * Quizzes that have been favourite, but the favourite system had not been added yet.
 *
 * @param navController Navigation controller to handle navigation actions.
 * @param quizzesViewModel ViewModel to handle the quiz data and logic
 * @author Callum Hodges
 */
@Composable
fun FavouriteScreenTopLevel(
    navController: NavHostController,
    quizzesViewModel: QuizViewModel = viewModel()
) {
    // get quiz list from ViewModel
    val quizList by quizzesViewModel.quizList.observeAsState(listOf())
    FavouriteScreen(
        quizzesList = quizList,
        navController = navController
    )
}

/**
 * Composable function to display the list of favourite quizzes.
 *
 * @param quizzesList List of quizzes to display.
 * @param navController Navigation controller to handle navigation actions.
 */
@Composable
fun FavouriteScreen(
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
            FloatingActionButton(onClick = { /* Handle FAB click */ }) {
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

            // LazyColumn to display the list of favourite quizzes
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
                            // Navigate to the PlayQuiz screen with the selected quiz ID
                            navController.navigate("${Screen.PlayQuiz.route}/${quiz.id}")
                        },
                        editAction = { editedQuiz ->
                            Log.d("QuizNavigation", "Navigating to EditQuiz with ID: ${quiz.id}")
                            // Navigate to the EditQuiz screen with the selected quiz ID
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
 * Preview function for the FavouriteScreen.
 */
@Preview
@Composable
fun FavouritePreview() {
    val navController = rememberNavController()
    QuizAppTheme(dynamicColor = false) {
        FavouriteScreen(navController = navController)
    }
}
