package uk.ac.aber.dcs.cs31620.quizapp.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Creates the page scaffold to contain top app bar, navigation drawer,
 * bottom navigation button, Floating Action Button, Snackbar and of course the page content.
 * @param navController To pass through the NavHostController since navigation is required
 * @param pageContent So that callers can plug in their own page content.
 * By default an empty lambda.
 * @author Chris Loftus
 */
@Composable
fun TopLevelScaffold(
    navController: NavHostController,
    floatingActionButton: @Composable () -> Unit = { },
    snackbarContent: @Composable (SnackbarData) -> Unit = {},
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState? = null,
    pageContent: @Composable (innerPadding: PaddingValues) -> Unit = {}
) {
    val drawerState =
        rememberDrawerState(initialValue = DrawerValue.Closed)

    MainPageNavigationDrawer(
        navController = navController,
        closeDrawer = {
            coroutineScope.launch {
                drawerState.close()
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                MainPageTopAppBar(onClick = {
                    coroutineScope.launch {
                        if (drawerState.isOpen) {
                            drawerState.close()
                        } else {
                            drawerState.open()
                        }
                    }
                })
            },
            floatingActionButton = floatingActionButton,
            snackbarHost = {
                snackbarHostState?.let {
                    SnackbarHost(hostState = snackbarHostState) { data ->
                        snackbarContent(data)
                    }
                }
            },
            bottomBar = {
                MainPageNavigationBar(navController)
            },
            content = { innerPadding -> pageContent(innerPadding) }
        )
    }
}

/**
 * Preview function for the TopLevelScaffold with a FAB. I added this.
 */
@Preview
@Composable
private fun TopLevelScaffoldPreview() {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Handle FAB click */ }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    )
}
