package com.example.vibramobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibramobile.Destination
import com.example.vibramobile.helpers.Navigator
import com.example.vibramobile.states.UiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpPasswordScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        UiState.setDisplayMediaPlayer(false)
        UiState.setDisplayNavigationBar(false)
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { scope.launch { Navigator.navigateUp() } }) {
                        Icon(
                            contentDescription = "",
                            imageVector = Icons.Default.ArrowBackIosNew,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Create a password",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            FormPasswordField(placeholder = "********")
            Text(
                text = "Enter password again",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            FormPasswordField(placeholder = "********")
            FormButton(
                onClick = { scope.launch { Navigator.navigate(Destination.LoginScreen) } },
                text = "Sign up"
            )
        }
    }
}

@Composable
fun FormPasswordField(modifier: Modifier = Modifier, placeholder: String) {
    val passwordHidden = remember { mutableStateOf(true) }
    OutlinedSecureTextField(
        modifier = Modifier.fillMaxWidth(),
        state = rememberTextFieldState(),
        placeholder = { Text(text = placeholder, color = Color.White) },
        trailingIcon = {
            IconButton(onClick = { passwordHidden.value = !passwordHidden.value }) {
                Icon(
                    imageVector = if (passwordHidden.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        },
        textObfuscationMode = if (passwordHidden.value) TextObfuscationMode.Hidden else TextObfuscationMode.Visible
    )
}