package com.example.vinachos.screens

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.vinachos.MenuActivity
import com.example.vinachos.OlvidasteActivity
import com.example.vinachos.R
import com.example.vinachos.RegistrarseActivity
import com.example.vinachos.data.UserPreferences
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController)  {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var isFormValid by remember { mutableStateOf(false) }

    // Leer datos de usuarios al iniciar la pantalla
    val usersList by userPreferences.userPreferencesFlow.collectAsState(initial = emptyList())

    // Validación de formulario
    fun validateForm() {
        emailError = when {
            email.isBlank() -> "El correo no puede estar vacío"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "El correo no es válido"
            else -> null
        }
        passwordError = when {
            password.isBlank() -> "La contraseña no puede estar vacía"
            else -> null
        }
        isFormValid = emailError == null && passwordError == null
    }

    // Función para vibración
    fun vibrate(context: Context, isSuccess: Boolean) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val vibrationEffect = if (isSuccess) {
                    VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
                } else {
                    VibrationEffect.createWaveform(longArrayOf(0, 100, 50, 100), -1)
                }
                vibrator.vibrate(vibrationEffect)
            } else {
                if (isSuccess) {
                    vibrator.vibrate(300) // Vibración única de 300ms
                } else {
                    vibrator.vibrate(longArrayOf(0, 100, 50, 100), -1) // Vibración patrón
                }
            }
        }
    }

    // Validar credenciales
    fun validateCredentials() {
        validateForm()
        if (isFormValid) {
            val user = usersList.find { it.correo == email && it.password == password }
            if (user != null) {
                val sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("userName", user.nombreCompleto)
                editor.putString("userEmail", user.correo)
                editor.apply()
                vibrate(context, true)
                val intent = Intent(context, MenuActivity::class.java)
                context.startActivity(intent)

                //(context as Activity).finish()

            } else {
                vibrate(context, false)
                emailError = "Credenciales incorrectas"
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF0F0F0)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.vinachos),
                contentDescription = "Logo Vinachos",
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
                    .padding(bottom = 32.dp)
            )
// Campo de Correo Electrónico
            val emailColor by animateColorAsState(targetValue = if (emailError == null) Color(0xFF8B0000) else Color(0xFFFFC107))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    validateForm()
                },
                label = { Text("Correo Electrónico", color = Color.Black) }, // Asegura que el label sea negro
                isError = emailError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Campo de entrada de Correo Electrónico" },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Icono de Correo Electrónico",
                        tint = emailColor
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = emailColor,  // Animación del color de borde cuando está enfocado
                    unfocusedBorderColor = emailColor, // Animación del color de borde cuando no está enfocado
                    cursorColor = emailColor,         // Animación del color del cursor
                    focusedLabelColor = Color.Black,  // Color del label cuando está enfocado
                    unfocusedLabelColor = Color.Black // Color del label cuando no está enfocado
                )
            )

            if (emailError != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(top = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Error de Correo",
                        tint = Color(0xFFFF5449)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = emailError ?: "",
                        color = Color(0xFFFF5449),
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


// Campo de Contraseña
            val passwordColor by animateColorAsState(targetValue = if (passwordError == null) Color(0xFF8B0000) else Color(0xFFFFC107))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    validateForm()
                },
                label = { Text("Contraseña", color = Color.Black) }, // Asegura que el label sea siempre negro
                isError = passwordError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Campo de entrada de Contraseña" },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Icono de Contraseña",
                        tint = passwordColor
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = passwordColor,  // Animación del color del borde
                    unfocusedBorderColor = passwordColor, // Borde cuando no está enfocado
                    cursorColor = passwordColor,         // Animación del color del cursor
                    focusedLabelColor = Color.Black,     // El label es negro cuando está enfocado
                    unfocusedLabelColor = Color.Black    // El label es negro cuando no está enfocado
                )
            )

            if (passwordError != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Error de Contraseña",
                        tint = Color(0xFFFF5449)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = passwordError ?: "",
                        color = Color(0xFFFF5449),
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            // Botón para Iniciar Sesión
            Button(
                onClick = { validateCredentials() },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) Color(0xFF8B0000) else Color(0xFF8B0000)
                )
            ) {
                Text("Iniciar Sesión", fontSize = 18.sp, color = Color(0xFF000000))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Texto para recuperación de contraseña y registro
            Text(
                "¿Olvidaste tu contraseña?",
                color = Color(0xFF969088),
                fontSize = 20.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    val intent = Intent(context, OlvidasteActivity::class.java)
                    context.startActivity(intent)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Registrarse",
                color = Color(0xFF969088),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    val intent = Intent(context, RegistrarseActivity::class.java)
                    context.startActivity(intent)
                }
            )
        }
    }
}
