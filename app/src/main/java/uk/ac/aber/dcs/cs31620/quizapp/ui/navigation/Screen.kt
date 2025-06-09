package uk.ac.aber.dcs.cs31620.quizapp.ui.navigation


/**
 * Wraps as objects, singletons for each screen used in
 * navigation. Each has a unique route.
 * @param route To pass through the route string
 * @author Chris Loftus
 */
/*Used Chris' class to amend the route links so that they would go
to specified quiz or question screens.

 */
sealed class Screen(
    val route: String
) {
    data object AllQuizzes : Screen("AllQuizzes")
    data object Favourite : Screen("favourite")
    data object Login : Screen("login")
    data object EditQuiz : Screen("EditQuiz/{id}")
    data object EditQuestion : Screen("EditQuestion/{questionId}")
    data object PlayQuiz : Screen("PlayQuiz/{id}")
}

val screens = listOf(
    Screen.AllQuizzes,
    Screen.Favourite,
)
