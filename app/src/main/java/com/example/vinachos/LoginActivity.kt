package com.example.vinachos

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.vinachos.data.UserPreferences
import com.example.vinachos.screens.LoginScreen
import com.example.vinachos.ui.theme.DyfTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.navigation.compose.rememberNavController // Importar NavController

class LoginActivity : ComponentActivity() {

    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPreferences = UserPreferences(this)

        // Obtener y mostrar la lista de usuarios al iniciar la aplicación
        CoroutineScope(Dispatchers.IO).launch {
            val allUsers = userPreferences.getUsers() // Llama a la función getUsers()
            allUsers.forEach { user ->
                Log.d("UsuariosRegistrados", "Nombre: ${user.nombreCompleto}, Correo: ${user.correo}, RUT: ${user.rut}")
            }

            //Función para limpiar la lista de usuarios (activarla cuando sea necesario)
            /*
            CoroutineScope(Dispatchers.IO).launch {
                userPreferences.clearUsers()  // Llama a clearUsers() para eliminar los usuarios
                Log.d("UsuariosRegistrados", "Lista de usuarios eliminada.")
            }
            */
        }

        // Configuración de la interfaz de usuario usando Compose
        setContent {
            DyfTheme {
                // Inicializar NavController
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Pasar el NavController a LoginScreen
                    LoginScreen(navController = navController)
                }
            }
        }
    }
}
