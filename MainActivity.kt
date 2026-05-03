package com.example.smartbus
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.smartbus.navigation.AppNavGraph
import com.example.smartbus.ui.theme.SmartbusTheme


import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            SmartbusTheme {
                AppNavGraph()
            }
        }
    }
}

