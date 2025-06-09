package uk.ac.aber.dcs.cs31620.quizapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.QuizAppTheme
import uk.ac.aber.dcs.cs31620.quizapp.R
import uk.ac.aber.dcs.cs31620.quizapp.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.quizapp.ui.navigation.screens

/**
 * Creates the navigation bar displayed at the bottom of the screen.
 * Currently has two items: the All Quizzes item and the Favourites item.
 * @param navController To pass through the NavHostController since navigation is required
 * @author Callum Hodges
 */
@Composable
fun MainPageNavigationBar(
    navController: NavHostController
) {
    val icons = mapOf(
        Screen.Favourite to IconGroup(
            filledIcon = Icons.Filled.Favorite,
            outlineIcon = Icons.Outlined.Favorite,
            label = stringResource(id = R.string.Favourites)
        ),
        Screen.AllQuizzes to IconGroup(
            filledIcon = Icons.Filled.Home,
            outlineIcon = Icons.Outlined.Home,
            label = stringResource(id = R.string.All_Quizzes)
        )
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        screens.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            val labelText = icons[screen]?.label ?: ""

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = icons[screen]?.filledIcon ?: Icons.Default.Book,
                        contentDescription = labelText,
                        tint = when (screen) {
                            Screen.AllQuizzes -> Color.Blue
                            Screen.Favourite -> Color.Red
                            else -> Color.Unspecified
                        }
                    )
                },
                label = { Text(labelText) },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun MainPageNavigationBarPreview() {
    val navController = rememberNavController()
    QuizAppTheme(dynamicColor = false) {
        MainPageNavigationBar(navController)
    }
}
