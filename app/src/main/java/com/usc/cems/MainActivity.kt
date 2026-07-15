package com.usc.cems

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.usc.cems.ui.navigation.NavigationGraph
import com.usc.cems.ui.theme.CarolianEventsManagementSystemTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarolianEventsManagementSystemTheme {
                val navController = rememberNavController()
                NavigationGraph(
                    navController = navController,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
