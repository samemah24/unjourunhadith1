package com.hadithdujour.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        NotificationHelper.createChannel(this)
        val prefs = Prefs(this)
        if (prefs.notificationsEnabled) {
            DailyScheduler.schedule(this)
        }
        HadithWidgetProvider.updateAll(this)

        setContent { HadithApp(prefs) }
    }
}

@Composable
fun HadithApp(prefs: Prefs) {
    val context = LocalContext.current
    var languageSetting by remember { mutableStateOf(prefs.language) }
    var favorites by remember { mutableStateOf(prefs.favorites) }
    var tab by rememberSaveable { mutableIntStateOf(0) }

    val language = Prefs.resolveLanguage(languageSetting)

    val toggleFavorite: (Int) -> Unit = { id ->
        favorites = if (id in favorites) favorites - id else favorites + id
        prefs.favorites = favorites
    }

    HadithTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = tab == 0,
                        onClick = { tab = 0 },
                        icon = { Icon(Icons.Filled.DateRange, contentDescription = null) },
                        label = { Text(stringResource(R.string.nav_today)) }
                    )
                    NavigationBarItem(
                        selected = tab == 1,
                        onClick = { tab = 1 },
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text(stringResource(R.string.nav_favorites)) }
                    )
                    NavigationBarItem(
                        selected = tab == 2,
                        onClick = { tab = 2 },
                        icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                        label = { Text(stringResource(R.string.nav_settings)) }
                    )
                }
            }
        ) { padding ->
            val contentModifier = Modifier.padding(padding)
            when (tab) {
                0 -> TodayScreen(
                    language = language,
                    favorites = favorites,
                    onToggleFavorite = toggleFavorite,
                    modifier = contentModifier
                )

                1 -> FavoritesScreen(
                    language = language,
                    favorites = favorites,
                    onToggleFavorite = toggleFavorite,
                    modifier = contentModifier
                )

                else -> SettingsScreen(
                    prefs = prefs,
                    languageSetting = languageSetting,
                    onLanguageChange = { code ->
                        languageSetting = code
                        prefs.language = code
                        HadithWidgetProvider.updateAll(context)
                    },
                    modifier = contentModifier
                )
            }
        }
    }
}
