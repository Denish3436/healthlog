// HealthLog App
package com.denish3436.healthlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.denish3436.healthlog.data.database.HealthDatabase
import com.denish3436.healthlog.data.repository.HealthRepository
import com.denish3436.healthlog.ui.HealthLogApp
import com.denish3436.healthlog.ui.theme.HealthLogTheme
import com.denish3436.healthlog.viewmodel.HealthViewModel
import com.denish3436.healthlog.viewmodel.HealthViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = HealthDatabase.getDatabase(this)
        val repository = HealthRepository(database.healthDao())
        val viewModelFactory = HealthViewModelFactory(repository)

        setContent {
            HealthLogTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: HealthViewModel = viewModel(factory = viewModelFactory)
                    HealthLogApp(viewModel = viewModel)
                }
            }
        }
    }
}