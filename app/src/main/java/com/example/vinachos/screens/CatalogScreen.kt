package com.example.vinachos.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun CatalogScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF0F0F0) // Fondo claro
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pantalla de Comprar",
                    fontSize = 24.sp,
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    // Acción para navegar al menú u otra pantalla
                    navController.navigate("menu")
                }) {
                    Text("Volver al menú")
                }
            }

            // Barra de navegación inferior
            NavigationBar(
                containerColor = Color(0xFF8B0000), // Color rojo para la barra inferior
                contentColor = Color.White
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = { navController.navigate("menu") } // Navegar al menú
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Search, contentDescription = "Explorar") },
                    label = { Text("Explorar") },
                    selected = false,
                    onClick = { /* Acción para explorar */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Cesta") },
                    label = { Text("Cesta") },
                    selected = true, // Establece la pestaña actual como seleccionada
                    onClick = { /* Acción para ver la cesta */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = false,
                    onClick = { /* Acción para ver perfil */ }
                )
            }
        }
    }
}
