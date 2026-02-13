package com.example.teamoodtracker.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.teamoodtracker.core.AppContainer
import com.example.teamoodtracker.core.TeaMoodViewModelFactory
import com.example.teamoodtracker.ui.add.AddLogRoute
import com.example.teamoodtracker.ui.add.AddLogViewModel
import com.example.teamoodtracker.ui.history.HistoryRoute
import com.example.teamoodtracker.ui.history.HistoryViewModel
import com.example.teamoodtracker.ui.home.HomeRoute
import com.example.teamoodtracker.ui.home.HomeViewModel
import com.example.teamoodtracker.ui.navigation.TeaMoodDestination

/*
 * Root compose app with scaffold and navigation.
 */
@Composable
fun TeaMoodApp(appContainer: AppContainer) {
  val navController = rememberNavController()
  val factory = TeaMoodViewModelFactory(appContainer)
  val homeViewModel: HomeViewModel = viewModel(factory = factory)
  val historyViewModel: HistoryViewModel = viewModel(factory = factory)
  val addLogViewModel: AddLogViewModel = viewModel(factory = factory)

  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route
  val showBottom = currentRoute != TeaMoodDestination.AddLog.route
  val showFab = currentRoute == TeaMoodDestination.Today.route
    || currentRoute == TeaMoodDestination.History.route

  Scaffold(
    bottomBar = {
      if (showBottom) {
        TeaMoodBottomBar(navController = navController)
      }
    },
    floatingActionButton = {
      if (showFab) {
        FloatingActionButton(
          onClick = {
            navController.navigate(TeaMoodDestination.AddLog.route)
          }
        ) {
          Icon(Icons.Default.Add, contentDescription = "Add log")
        }
      }
    }
  ) { paddingValues ->
    NavHost(
      navController = navController,
      startDestination = TeaMoodDestination.Today.route
    ) {
      composable(TeaMoodDestination.Today.route) {
        androidx.compose.foundation.layout.Box(
          modifier = Modifier.padding(paddingValues)
        ) {
          HomeRoute(viewModel = homeViewModel)
        }
      }
      composable(TeaMoodDestination.History.route) {
        androidx.compose.foundation.layout.Box(
          modifier = Modifier.padding(paddingValues)
        ) {
          HistoryRoute(viewModel = historyViewModel)
        }
      }
      composable(TeaMoodDestination.AddLog.route) {
        androidx.compose.foundation.layout.Box(
          modifier = Modifier.padding(paddingValues)
        ) {
          AddLogRoute(
            viewModel = addLogViewModel,
            onSaved = { navController.popBackStack() }
          )
        }
      }
    }
  }
}

/*
 * Bottom navigation for Today and History.
 */
@Composable
private fun TeaMoodBottomBar(navController: NavHostController) {
  val currentRoute = navController.currentBackStackEntryAsState()
    .value?.destination?.route

  val destinations = listOf(
    TeaMoodDestination.Today to Icons.Default.Home,
    TeaMoodDestination.History to Icons.Default.History
  )

  NavigationBar {
    destinations.forEach { (destination, icon) ->
      NavigationBarItem(
        selected = currentRoute == destination.route,
        onClick = {
          navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) {
              saveState = true
            }
            launchSingleTop = true
            restoreState = true
          }
        },
        icon = { Icon(icon, contentDescription = destination.label) },
        label = { Text(destination.label) }
      )
    }
  }
}
