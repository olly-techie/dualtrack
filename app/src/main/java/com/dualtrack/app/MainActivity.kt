package com.dualtrack.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dualtrack.app.ui.DualTrackApp
import com.dualtrack.app.ui.DualTrackViewModel
import com.dualtrack.app.ui.repositoryFor
import com.dualtrack.app.ui.theme.DualTrackTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repo = repositoryFor(application)

        setContent {
            DualTrackTheme {
                val vm: DualTrackViewModel = viewModel(
                    factory = DualTrackViewModel.Factory(repo),
                )
                DualTrackApp(vm = vm)
            }
        }
    }
}
