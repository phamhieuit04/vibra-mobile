package com.example.vibramobile.presentation.screen

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibramobile.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpPasswordScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            contentDescription = "",
                            imageVector = Icons.Default.ArrowBackIosNew,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0f)
                )
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
                text = stringResource(R.string.auth_create_password),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            FormPasswordField(placeholder = stringResource(R.string.password_mask_placeholder))
            Text(
                text = stringResource(R.string.auth_enter_password_again),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            FormPasswordField(placeholder = stringResource(R.string.password_mask_placeholder))
            FormButton(
                onClick = navigateToLogin,
                text = stringResource(R.string.action_sign_up)
            )
        }
    }
}

@Composable
fun FormPasswordField(modifier: Modifier = Modifier, placeholder: String) {
    val passwordHidden = remember { mutableStateOf(true) }
    OutlinedSecureTextField(
        modifier = modifier.fillMaxWidth(),
        state = rememberTextFieldState(),
        placeholder = {
            Text(text = placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        trailingIcon = {
            IconButton(onClick = { passwordHidden.value = !passwordHidden.value }) {
                Icon(
                    imageVector = if (passwordHidden.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        textObfuscationMode = if (passwordHidden.value) TextObfuscationMode.Hidden else TextObfuscationMode.Visible
    )
}