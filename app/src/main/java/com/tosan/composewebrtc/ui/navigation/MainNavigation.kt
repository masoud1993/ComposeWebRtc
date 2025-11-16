package com.tosan.composewebrtc.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.tosan.composewebrtc.ui.screen.ClientScreen
import com.tosan.composewebrtc.ui.screen.HostScreen
import com.tosan.composewebrtc.ui.screen.MainScreen
import kotlinx.serialization.Serializable

@Serializable
object Main

@Serializable
object Host

@Serializable
data class Client(val server: String)

@Composable
fun DemoNavHost(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = Main) {

        composable<Main> {
            MainScreen(
                navigateToHost = { navController.navigate(Host) },
                navigateToClient = { navController.navigate(Client(it)) }
            )
        }

        composable<Host> {
            HostScreen(
                navigateToMain = { navController.navigate(Main) }
            )
        }

        composable<Client> {

            val server = it.toRoute<Client>().server

            ClientScreen(server)
        }

    }
}