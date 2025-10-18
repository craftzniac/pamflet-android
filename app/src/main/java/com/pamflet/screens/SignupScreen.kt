package com.pamflet.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pamflet.ui.theme.Gray500
import com.pamflet.ui.theme.Purple500
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import com.pamflet.components.SimpleTopAppBar


class SignupScreenViewModel : ViewModel() {
    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password


    private val _confirmPassword = mutableStateOf("")
    val confirmPassword: State<String> = _confirmPassword

    fun updateEmail(update: String) {
        _email.value = update
    }

    fun updatePassword(update: String) {
        _password.value = update
    }

    fun updateConfirmPassword(update: String) {
        _confirmPassword.value = update
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    onNavigateToLoginScreen: () -> Unit,
    onNavigateToManageDecksScreen: () -> Unit,
    viewModel: SignupScreenViewModel = viewModel(),
) {
    Scaffold(
        topBar = { SimpleTopAppBar(title = "Pamflet", isShowPamfletLogo = true) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Text("Signup", fontSize = 24.sp, fontWeight = FontWeight.Medium)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PTextField(
                        value = viewModel.email.value,
                        label = "Email",
                        onValueChange = { viewModel.updateEmail(it) }
                    )
                    PTextField(
                        value = viewModel.password.value,
                        label = "Password",
                        onValueChange = { viewModel.updatePassword(it) }
                    )
                    PTextField(
                        value = viewModel.password.value,
                        label = "Confirm Password",
                        onValueChange = { viewModel.updateConfirmPassword(it) }
                    )
                    Button(
                        onClick = { onNavigateToManageDecksScreen() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Continue", fontSize = 16.sp, modifier = Modifier.padding(4.dp))
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(0.5.dp)
                                .background(color = Gray500)
                                .weight(1f)
                        ) {}
                        Text("Or")
                        Box(
                            modifier = Modifier
                                .height(0.5.dp)
                                .background(color = Gray500)
                                .weight(1f)
                        ) {}
                    }
                    GoogleSignupButton()
                    Spacer(Modifier.height(8.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            "Already have an account?",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            modifier = Modifier.padding(0.dp),
                            onClick = {
                                onNavigateToLoginScreen()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            interactionSource = remember { MutableInteractionSource() },
                            contentPadding = PaddingValues(0.dp),
                        ) {
                            Text(
                                "Login",
                                textDecoration = TextDecoration.Underline,
                                color = Purple500,
                                lineHeight = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
