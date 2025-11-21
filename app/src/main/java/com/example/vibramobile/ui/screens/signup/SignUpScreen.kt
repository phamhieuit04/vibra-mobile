package com.example.vibramobile.ui.screens.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibramobile.R
import com.example.vibramobile.helpers.Navigator
import com.example.vibramobile.Destination
import com.example.vibramobile.ui.screens.login.FormButton
import com.example.vibramobile.ui.screens.login.FormInput
import com.example.vibramobile.ui.screens.login.SocialMethod
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(modifier: Modifier = Modifier) {
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
                Text(text = "Already have an account?", color = Color.White, fontSize = 16.sp)
                TextButton(onClick = {
                    scope.launch {
                        Navigator.popBackStack(
                            destination = Destination.LoginScreen,
                            inclusive = false
                        )
                    }
                }) {
                    Text(
                        text = "Log in",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(Modifier.height(32.dp))
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
                    text = "Sign up to",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    lineHeight = 32.sp
                )
                Text(
                    text = "start listening",
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
                FormInput(
                    placeholder = "What's your email?",
                )
                FormButton(
                    onClick = { scope.launch { Navigator.navigate(destination = Destination.SignUpPasswordScreen) } },
                    text = "Continue"
                )
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

//@Preview(showBackground = true, device = "id:pixel_3", backgroundColor = 0xff000000)
//@Composable
//fun Preview(modifier: Modifier = Modifier) {
//    SignUpScreen()
//}