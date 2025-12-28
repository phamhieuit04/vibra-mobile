package com.example.vibramobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vibramobile.Destination
import com.example.vibramobile.R
import com.example.vibramobile.helpers.Navigator
import com.example.vibramobile.states.UiState
import com.example.vibramobile.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        UiState.setDisplayMediaPlayer(false)
        UiState.setDisplayNavigationBar(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(32.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .weight(0.6f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.scale(1.2f),
                painter = painterResource(R.drawable.logo),
                contentDescription = "",
                colorFilter = ColorFilter.tint(color = Color.White)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Millions of songs.",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                lineHeight = 32.sp
            )
            Text(
                text = "Free on Vibra.",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                lineHeight = 32.sp
            )
        }
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { scope.launch { Navigator.navigate(Destination.SignUpScreen) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffbc4d15))
            ) {
                Text(text = "Sign up free", color = Color.White, fontSize = 16.sp)
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = { scope.launch { Navigator.navigate(Destination.LoginScreen) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Log in", color = Color.White, fontSize = 16.sp)
            }
        }
        Spacer(Modifier.height(32.dp))
    }
}