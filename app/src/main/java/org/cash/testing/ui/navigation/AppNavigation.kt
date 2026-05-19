package org.cash.testing.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.cash.testing.ui.screen.LoginScreen
import org.cash.testing.ui.screen.TaskListScreen
import org.cash.testing.ui.screen.TaskDetailScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Login,
        modifier = modifier
    ) {
        composable<Login> {
            LoginScreen(
                onNavigateToMain = {
                    navController.navigate(TaskList) {
                        popUpTo(Login) { inclusive = true }
                    }
                }
            )
        }
        
        composable<TaskList> {
            TaskListScreen(
                onNavigateToDetail = { taskId ->
                    navController.navigate(TaskDetail(taskId = taskId))
                },
                onLogout = {
                    navController.navigate(Login) {
                        popUpTo(TaskList) { inclusive = true }
                    }
                }
            )
        }
        
        composable<TaskDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<TaskDetail>()
            TaskDetailScreen(
                taskId = route.taskId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
