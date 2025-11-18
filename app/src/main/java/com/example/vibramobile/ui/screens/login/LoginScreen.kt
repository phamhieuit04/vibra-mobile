package com.example.vibramobile.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vibramobile.R
import com.example.vibramobile.helpers.Navigator
import com.example.vibramobile.ui.Destination
import com.example.vibramobile.viewmodels.AuthViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

object LoginStep {
    @Serializable
    object Email

    @Serializable
    object Password
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel<AuthViewModel>()
) {
    val scope = rememberCoroutineScope()

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
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Don't have an account?", color = Color.White, fontSize = 16.sp)
                TextButton(onClick = {
                    scope.launch {
                        Navigator.popBackStack(
                            destination = Destination.SignUpScreen,
                            inclusive = false
                        )
                    }
                }) {
                    Text(
                        text = "Sign up",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .scale(1.2f)
                        .fillMaxWidth(),
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(color = Color.White)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Login to Vibra",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    lineHeight = 32.sp
                )
            }
            Spacer(Modifier.height(24.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = LoginStep.Email) {
                    composable<LoginStep.Email> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FormInput(placeholder = "What's your email?")
                            FormButton(
                                onClick = { navController.navigate(LoginStep.Password) },
                                text = "Continue"
                            )
                        }
                    }
                    composable<LoginStep.Password> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FormInput(placeholder = "Enter your password")
                            FormButton(onClick = {
                                viewModel.login(
                                    email = "tomnguyenhieu2004@gmail.com",
                                    password = "12345678"
                                )
                            }, text = "Log in")
                        }
                    }
                }
                Text(
                    text = "or",
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
                SocialMethod(
                    onClick = {},
                    provider = "Google",
                    painter = painterResource(R.drawable.google_logo)
                )
                SocialMethod(
                    onClick = {},
                    provider = "Facebook",
                    painter = painterResource(R.drawable.facebook_logo)
                )
            }
        }
    }
}

@Composable
fun SocialMethod(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    provider: String,
    painter: Painter
) {
    OutlinedButton(
        onClick = onClick,
        contentPadding = PaddingValues(top = 14.dp, bottom = 14.dp, start = 24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.CenterStart
        ) {
            Image(
                modifier = Modifier.size(28.dp),
                alignment = Alignment.CenterStart,
                contentDescription = "",
                painter = painter
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Continue with $provider",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}

@Composable
fun FormInput(
    modifier: Modifier = Modifier,
    placeholder: String,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        state = rememberTextFieldState(),
        placeholder = { Text(text = placeholder, color = Color.White) }
    )
}

@Composable
fun FormButton(onClick: () -> Unit, modifier: Modifier = Modifier, text: String) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xffbc4d15))
    ) {
        Text(text = text, color = Color.White, fontSize = 16.sp)
    }
}