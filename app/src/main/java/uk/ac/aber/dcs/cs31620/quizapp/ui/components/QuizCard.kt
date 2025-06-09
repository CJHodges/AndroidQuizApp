package uk.ac.aber.dcs.cs31620.quizapp.ui.components

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.compose.QuizAppTheme
import uk.ac.aber.dcs.cs31620.quizapp.R
import uk.ac.aber.dcs.cs31620.quizapp.model.Quiz
import uk.ac.aber.dcs.cs31620.quizapp.ui.navigation.Screen

/**
 * Composable function to display a quiz card.
 * The quiz cards display the relevant quiz name, description and image.
 * The quiz card also offers 3 options, clicking on the card itself will launch the quiz
 * Pressing edit button will take you to the edit quiz screen, pressing the delete button
 * would delete the quiz if implemented to do so.
 * @param modifier Modifier for styling the card.
 * @param quiz The quiz data to display.
 * @param navController Navigation controller to handle navigation actions.
 * @param imagePath Path to where the quiz images would be stored.
 * @param selectAction Action to perform when the quiz card is selected.
 * @param editAction Action to perform when the quiz card edit button is selected.
 * @param deleteAction Action to perform when the quiz card is deleted button is selected.
 * @author Callum Hodges
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun QuizCard(
    modifier: Modifier = Modifier,
    quiz: Quiz,
    navController: NavHostController,
    imagePath: String = "file:///android_asset/images/",
    selectAction: (Quiz) -> Unit = {},
    editAction: (Quiz) -> Unit = {},
    deleteAction: (Quiz) -> Unit = {}
) {
    // Creating a card with specified modifiers and the navigation link when clicked on
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("${Screen.PlayQuiz.route}/${quiz.id}") {
                    launchSingleTop = true
                }
            }
    ) {
        // Column to arrange elements vertically
        Column(
            modifier = Modifier
                .clickable { selectAction(quiz) }
                .padding(16.dp)
        ) {
            // Row for the quiz name and options menu icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = quiz.name,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )

                IconButton(
                    onClick = { /* Option menu code would be handled here */ }
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = stringResource(R.string.options_menu)
                    )
                }
            }

            // Displaying the quiz image
            GlideImage(
                model = Uri.parse(imagePath + quiz.imagePath),
                contentDescription = stringResource(R.string.quiz_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .padding(top = 8.dp)
            )

            // Displaying the quiz description
            Text(
                text = quiz.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Row for edit and delete buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                //navigation link for edit button
                Button(
                    onClick = {
                        //Log debugging to check the if the user is being navigated to the correct quiz
                        Log.d("QuizNavigation", "Navigating to EditQuiz with ID: ${quiz.id}")
                        navController.navigate("${Screen.EditQuiz.route}/${quiz.id}") {
                            launchSingleTop = true
                        }
                        editAction(quiz)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(text = stringResource(R.string.edit_Quiz))
                }

                Button(
                    onClick = { deleteAction(quiz) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(text = stringResource(R.string.Delete_Quiz))
                }
            }
        }
    }
}

/**
 * Preview function for the QuizCard.
 */
@Preview
@Composable
private fun QuizCardPreview() {
    val navController = rememberNavController()
    QuizAppTheme(dynamicColor = false) {
        QuizCard(
            quiz = Quiz(
                name = "Python Quiz",
                description = "An introductory quiz to Python programming.",
                imagePath = "pythonimage.jpg"
            ),
            navController = navController
        )
    }
}
