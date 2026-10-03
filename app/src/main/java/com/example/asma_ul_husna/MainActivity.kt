package com.example.asma_ul_husna

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.asma_ul_husna.data.local.NamesJsonDataSource
import com.example.asma_ul_husna.data.local.QuizJsonDataSource
import com.example.asma_ul_husna.data.local.UserPreferencesRepository
import com.example.asma_ul_husna.data.model.AppTheme
import com.example.asma_ul_husna.data.remote.NamesApiDataSource
import com.example.asma_ul_husna.data.repository.NamesRepositoryImpl
import com.example.asma_ul_husna.data.repository.QuizRepositoryImpl
import com.example.asma_ul_husna.domain.repository.NamesRepository
import com.example.asma_ul_husna.domain.repository.QuizRepository
import com.example.asma_ul_husna.navigation.AppNavigation
import com.example.asma_ul_husna.ui.theme.AsmaulHusnaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val jsonDataSource = NamesJsonDataSource(applicationContext)
        val remoteDataSource = NamesApiDataSource()
        val userPreferencesRepository = UserPreferencesRepository(applicationContext)
        val namesRepository: NamesRepository = NamesRepositoryImpl(
            localDataSource = jsonDataSource,
            remoteDataSource = remoteDataSource,
            userPreferencesRepository = userPreferencesRepository
        )

        val quizJsonDataSource = QuizJsonDataSource(applicationContext)
        val quizRepository: QuizRepository = QuizRepositoryImpl(
            localDataSource = quizJsonDataSource,
            userPreferencesRepository = userPreferencesRepository
        )

        setContent {
            val appTheme by namesRepository.getThemePreference().collectAsState(initial = AppTheme.SYSTEM)

            AsmaulHusnaTheme(appTheme = appTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(
                        repository = namesRepository,
                        quizRepository = quizRepository
                    )
                }
            }
        }
    }
}