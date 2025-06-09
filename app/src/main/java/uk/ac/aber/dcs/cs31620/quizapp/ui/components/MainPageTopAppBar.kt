package uk.ac.aber.dcs.cs31620.quizapp.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.aber.dcs.cs31620.quizapp.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import com.example.compose.QuizAppTheme

/**
 * Represents a top app bar component using M3 CenterAlignedTopAppBar.
 * Has a menu button icon and the app name.
 * @param onClick: provides the behaviour for the menu icon or
 * an empty lambda if not provided.
 * @author Chris Loftus
 */
@Composable
fun MainPageTopAppBar(
    onClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(stringResource(id = R.string.app_name)) //I changed the name of the quiz, but aside from that this class is unchanged from Chris'
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription =
                    stringResource(R.string.nav_drawer_menu)
                )
            }
        }
    )
}

@Preview
@Composable
private fun MainPageTopAppBarPreview() {
    QuizAppTheme(dynamicColor = false) {
        MainPageTopAppBar()
    }
}