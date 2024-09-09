package com.example.vinachos

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.vinachos.data.UserPreferences
import com.example.vinachos.screens.MenuScreen
import com.example.vinachos.ui.theme.DyfTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vinachos.screens.CatalogScreen
import com.example.vinachos.screens.LoginScreen
import com.example.vinachos.screens.ProfileScreen

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )

        enableEdgeToEdge()
        setContent {
            DyfTheme {
                val userPreferences = UserPreferences(this)
                val navController = rememberNavController() // Crear NavController

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Configurar NavHost
                    NavHost(navController = navController, startDestination = "menu") {
                        composable("menu") {
                            MenuScreen(userPreferences = userPreferences, navController = navController)
                        }
                        composable("catalogo") {
                            // Pantalla de catálogo
                            CatalogScreen(navController = navController)
                        }
                        composable("perfil") {
                            ProfileScreen(navController = navController, userPreferences = userPreferences)
                        }
                        composable("login") {
                            LoginScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
