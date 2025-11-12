package com.tosan.composewebrtc.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tosan.composewebrtc.ui.screen.ClientScreen
import com.tosan.composewebrtc.ui.screen.HostScreen
import com.tosan.composewebrtc.ui.screen.MainScreen
import kotlinx.serialization.Serializable

@Serializable
object Main

@Serializable
object Host

@Serializable
object Client

@Composable
fun DemoNavHost(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = Main) {

        composable<Main> {
            MainScreen()
        }

        composable<Host> {
            HostScreen()
        }

        composable<Client> {
            ClientScreen()
        }

    }
}