package com.example.vinachos.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vinachos.data.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart


@Composable
fun ProfileScreen(navController: NavController, userPreferences: UserPreferences) {
    // Variable para gestionar la selección de la barra de navegación
    var selectedItem by remember { mutableStateOf("perfil") } // Inicializa como "perfil" ya que estamos en la pantalla de perfil

    // Obtener el usuario actual desde UserPreferences
    val user = runBlocking { userPreferences.getUsers().firstOrNull() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF0F0F0) // Fondo claro
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Mostrar nombre y correo del usuario
                Text(
                    text = "Nombre: ${user?.nombreCompleto ?: "N/A"}",
                    fontSize = 24.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Correo: ${user?.correo ?: "N/A"}",
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Navegar a la pantalla de "Mi cuenta"
                        navController.navigate("mi_cuenta")
                    }
                ) {
                    Text(text = "Mi cuenta")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Cerrar sesión y redirigir al login
                        CoroutineScope(Dispatchers.IO).launch {
                            userPreferences.clearUserSession() // Limpiar la sesión del usuario
                            navController.navigate("login") { // Redirigir al login
                                popUpTo("perfil") { inclusive = true } // Eliminar la pantalla de perfil del backstack
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(text = "Cerrar sesión", color = Color.White)
                }
            }

            // Barra de navegación
            NavigationBar(
                containerColor = Color(0xFF8B0000),
                contentColor = Color.White
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = selectedItem == "inicio",
                    onClick = {
                        selectedItem = "inicio"
                        navController.navigate("menu") // Navegar a la pantalla de inicio
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Search, contentDescription = "Explorar") },
                    label = { Text("Explorar") },
                    selected = selectedItem == "explorar",
                    onClick = {
                        selectedItem = "explorar"
                        navController.navigate("explorar")
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Catalogo") },
                    label = { Text("Catalogo") },
                    selected = selectedItem == "catalogo",
                    onClick = {
                        selectedItem = "catalogo"
                        navController.navigate("catalogo")
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = selectedItem == "perfil", // Esto debe mantenerse seleccionado
                    onClick = {
                        selectedItem = "perfil"
                        // Mantener en la pantalla de perfil
                    }
                )
            }
        }
    }
}
