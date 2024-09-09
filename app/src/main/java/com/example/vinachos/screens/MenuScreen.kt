package com.example.vinachos.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vinachos.R
import com.example.vinachos.data.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(userPreferences: UserPreferences, navController: NavController) {
    var currentImageIndex by remember { mutableStateOf(0) }
    val images = listOf(R.drawable.vinachos, R.drawable.vinachos_red, R.drawable.vinachos_yellow)

    val transition = rememberInfiniteTransition(label = "imageTransition")
    val alpha by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "alphaAnimation"
    )

    var selectedItem by remember { mutableStateOf("inicio") } // Manejar la selección del ítem de navegación
    var showProfileDialog by remember { mutableStateOf(false) } // Mostrar el diálogo para perfil

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            currentImageIndex = (currentImageIndex + 1) % images.size
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imagen con efecto de transición
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = painterResource(id = images[currentImageIndex]),
                        contentDescription = "Vino destacado",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(alpha = alpha)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Descubre la excelencia",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1E1E)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Explora nuestra selección de vinos premium",
                    fontSize = 16.sp,
                    color = Color(0xFF5E5E5E)
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        // Navegar a la pantalla de catálogo
                        navController.navigate("catalogo")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Ver catálogo",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
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
                        // Mantener en la pantalla de inicio
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
                    selected = selectedItem == "perfil",
                    onClick = {
                        selectedItem = "perfil"
                        showProfileDialog = true // Mostrar el diálogo para opciones de perfil
                    }
                )
            }
        }
    }

    // Mostrar el diálogo de perfil
    if (showProfileDialog) {
        AlertDialog(
            onDismissRequest = {
                showProfileDialog = false // Cerrar el diálogo al hacer clic afuera
            },
            title = { Text("Perfil de Usuario") },
            text = { Text("Seleccione una opción") },
            confirmButton = {
                TextButton(onClick = {
                    // Navegar a la pantalla de perfil
                    navController.navigate("perfil")
                    showProfileDialog = false // Cerrar el diálogo
                }) {
                    Text("Mi Cuenta")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    // Cerrar sesión (solo limpiar la sesión activa)
                    CoroutineScope(Dispatchers.IO).launch {
                        userPreferences.clearUserSession() // Limpiar la sesión del usuario
                        navController.navigate("login") { // Redirigir al login
                            popUpTo("menu") { inclusive = true } // Eliminar pantallas anteriores del backstack
                        }
                    }
                    showProfileDialog = false // Cerrar el diálogo
                }) {
                    Text("Cerrar Sesión", color = Color.Red)
                }
            }
        )
    }
}
