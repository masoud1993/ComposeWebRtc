package com.tosan.composewebrtc.ui.screen

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    navigateToHost: () -> Unit,
    navigateToClient: (String) -> Unit,
) {
    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (!permissions.all { it.value }) {
            Toast.makeText(
                context,
                "Camera and Microphone permissions are required",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(key1 = Unit) {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            )
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFEFEFEF)), // Light background color
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Local Video Call using WebRTC",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(30.dp),
            color = Color.DarkGray,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Simply connect to same router or use hotspot to connect two devices to each other",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, start = 20.dp, end = 20.dp),
            color = Color.Gray,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Join As",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )

            // Host button
            Button(
                onClick = {
                    navigateToHost()
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Text(text = "Host", Modifier.fillMaxSize(), textAlign = TextAlign.Center)
            }

            // Or text
            Text(
                text = "Or",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .height(30.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )

            // Address input
            var addressState by remember { mutableStateOf("") }
            TextField(
                value = addressState,
                onValueChange = { addressState = it },
                placeholder = { Text(text = "Enter Host Address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .clip(RoundedCornerShape(8.dp))

            )

            Button(
                onClick = {
                    if (addressState.isEmpty()) {
                        Toast.makeText(context, "Enter Server Address", Toast.LENGTH_SHORT).show()
                    } else {
                        navigateToClient(addressState)
                    }
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Text(text = "Client", Modifier.fillMaxSize(), textAlign = TextAlign.Center)
            }
        }
    }
}