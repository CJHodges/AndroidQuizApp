package uk.ac.aber.dcs.cs31620.quizapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.QuizAppTheme
import uk.ac.aber.dcs.cs31620.quizapp.model.QuizViewModel
import uk.ac.aber.dcs.cs31620.quizapp.ui.Favourite.FavouriteScreenTopLevel
import uk.ac.aber.dcs.cs31620.quizapp.ui.authentication.LoginScreen
import uk.ac.aber.dcs.cs31620.quizapp.ui.Quiz.AllQuizzesScreenTopLevel
import uk.ac.aber.dcs.cs31620.quizapp.ui.editQuestion.EditQuestionScreenTopLevel
import uk.ac.aber.dcs.cs31620.quizapp.ui.editQuiz.EditQuizScreenTopLevel
import uk.ac.aber.dcs.cs31620.quizapp.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.quizapp.ui.playQuiz.PlayQuizScreenTopLevel

//test

/**
 * Starting activity class. Entry point for the app. This was built using Chris' class
 * as a baseline, but was changed to suit my code.
 * @author Callum Hodges
 */
// MainActivity that hosts the composable content and sets up the navigation graph
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizAppTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BuildNavigationGraph()
                }
            }
        }
    }

    /**
     * Composable function to build the navigation graph for the app.
     *
     * @param quizViewModel ViewModel to handle quiz data and logic
     * @param navController Navigation controller to manage navigation actions
     * @author Callum Hodges
     */
    @Composable
    fun BuildNavigationGraph(
        quizViewModel: QuizViewModel = viewModel(),
        navController: NavHostController = rememberNavController()
    ) {
        val startDestination = remember { Screen.AllQuizzes.route }

        // Set up the navigation host with the start destination and routes
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            // Composable for AllQuizzes screen
            composable(Screen.AllQuizzes.route) {
                AllQuizzesScreenTopLevel(navController, quizViewModel)
            }
            // Composable for Favourite screen
            composable(Screen.Favourite.route) {
                FavouriteScreenTopLevel(navController, quizViewModel)
            }
            // Composable for Login screen
            composable(Screen.Login.route) {
                LoginScreen(navController)
            }
            // Composable for PlayQuiz screen with dynamic ID
            composable(Screen.PlayQuiz.route + "/{id}") { backStackEntry ->
                //Logcat Debugger statement to check if QuizID is correct
                val quizId = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
                Log.d("QuizNavigation", "Navigating to PlayQuiz with ID: $quizId")
                PlayQuizScreenTopLevel(navController, quizId)
            }
            // Composable for EditQuiz screen with dynamic ID
            composable(Screen.EditQuiz.route + "/{id}") { backStackEntry ->
                //Logcat Debugger statement to check if QuizID is correct
                val quizId = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
                Log.d("QuizNavigation", "Navigating to EditQuiz with ID: $quizId")
                EditQuizScreenTopLevel(navController, quizId)
            }
            // Composable for EditQuestion screen with dynamic question ID
            composable(Screen.EditQuestion.route + "/{questionId}") { backStackEntry ->
                //Logcat Debugger statement to check if QuestionID is correct
                val questionId = backStackEntry.arguments?.getString("questionId")?.toInt() ?: 0
                Log.d("QuestionNavigation", "Navigating to EditQuestion with ID: $questionId")
                EditQuestionScreenTopLevel(navController = navController, questionId = questionId)
            }
        }
    }
}
