package com.pandacorp.knowui.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pandacorp.knowui.R
import com.pandacorp.knowui.presentation.ui.theme.WhiteRippleTheme
import com.pandacorp.knowui.presentation.viewmodel.LoginViewModel
import com.pandacorp.knowui.utils.Validation
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    vm: LoginViewModel = koinViewModel(),
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(0.8f),
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color.White.copy(0.9f),
                selectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.secondary,
                    backgroundColor = MaterialTheme.colorScheme.secondary.copy(0.5f)
                ),
                cursorColor = MaterialTheme.colorScheme.secondary,
            )

            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 14.dp),
                text = stringResource(if (vm.isSignIn) R.string.signIn else R.string.signUp),
                fontSize = 24.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.weight(1f, true))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = vm.email,
                isError = vm.emailErrorMessage != null,
                colors = textFieldColors,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onValueChange = {
                    vm.email = it
                    vm.validateEmail()
                },
                label = { Text(text = stringResource(R.string.email), color = Color.White) },
                placeholder = { Text(text = stringResource(R.string.email)) },
                supportingText = {
                    vm.emailErrorMessage?.let {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = vm.password,
                colors = textFieldColors,
                isError = vm.passwordErrorMessage != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = {
                    vm.password = it
                    vm.validatePassword()
                },
                label = { Text(text = stringResource(R.string.password), color = Color.White) },
                placeholder = { Text(text = stringResource(R.string.password)) },
                supportingText = {
                    vm.passwordErrorMessage?.let {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                visualTransformation = if (vm.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (vm.isPasswordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    IconButton(onClick = { vm.isPasswordVisible = !vm.isPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                },
            )

            CompositionLocalProvider(LocalRippleTheme provides WhiteRippleTheme()) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .padding(top = 12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    enabled = Validation.isValidInput(vm.email, vm.password),
                    onClick = {

                    },
                ) {
                    Text(
                        text = stringResource(if (vm.isSignIn) R.string.signIn else R.string.signUp),
                        color = Color.White
                    )
                }
            }
            CompositionLocalProvider(LocalRippleTheme provides WhiteRippleTheme()) {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    onClick = {
                        vm.isSkipped = true
                    },
                ) {
                    Text(stringResource(id = R.string.skip), color = Color.White)
                }
            }

            Spacer(modifier = Modifier.weight(1f, true))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(if (vm.isSignIn) R.string.signUpSuggestion else R.string.signInSuggestion), color = Color.White)

                CompositionLocalProvider(LocalRippleTheme provides WhiteRippleTheme()) {
                    TextButton(
                        modifier = Modifier.padding(start = 8.dp, end = 4.dp),
                        onClick = {
                            vm.isSignIn = !vm.isSignIn
                        }
                    ) {
                        Text(
                            text = stringResource(if (vm.isSignIn) R.string.signUp else R.string.signIn),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}