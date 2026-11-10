package com.hadithdujour.app

import android.Manifest
import android.app.TimePickerDialog
import android.text.format.DateFormat
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun SettingsScreen(
    prefs: Prefs,
    languageSetting: String,
    onLanguageChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var notifEnabled by remember { mutableStateOf(prefs.notificationsEnabled) }
    var hour by remember { mutableIntStateOf(prefs.notifHour) }
    var minute by remember { mutableIntStateOf(prefs.notifMinute) }
    var pendingAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        val action = pendingAction
        pendingAction = null
        if (granted) {
            action?.invoke()
        } else {
            Toast.makeText(context, R.string.permission_denied, Toast.LENGTH_LONG).show()
        }
    }

    // Exécute l'action si les notifications sont autorisées, sinon demande la permission d'abord.
    fun withPermission(action: () -> Unit) {
        if (NotificationHelper.canPost(context)) {
            action()
        } else {
            pendingAction = action
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val autoLabel = stringResource(R.string.settings_language_auto)
    val languageOptions = listOf(
        Prefs.LANGUAGE_AUTO to autoLabel,
        "fr" to "Français",
        "en" to "English",
        "de" to "Deutsch",
        "es" to "Español"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.settings_language),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        languageOptions.forEach { (code, label) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = languageSetting == code,
                        onClick = { onLanguageChange(code) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = languageSetting == code, onClick = null)
                Spacer(Modifier.width(12.dp))
                Text(text = label, style = MaterialTheme.typography.bodyLarge)
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.settings_notification),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.settings_notification_desc),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.width(12.dp))
            Switch(
                checked = notifEnabled,
                onCheckedChange = { wantOn ->
                    if (wantOn) {
                        withPermission {
                            notifEnabled = true
                            prefs.notificationsEnabled = true
                            DailyScheduler.schedule(context)
                        }
                    } else {
                        notifEnabled = false
                        prefs.notificationsEnabled = false
                        DailyScheduler.cancel(context)
                    }
                }
            )
        }

        if (notifEnabled) {
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = {
                    TimePickerDialog(
                        context,
                        { _, selectedHour, selectedMinute ->
                            hour = selectedHour
                            minute = selectedMinute
                            prefs.notifHour = selectedHour
                            prefs.notifMinute = selectedMinute
                            DailyScheduler.schedule(context)
                        },
                        hour,
                        minute,
                        DateFormat.is24HourFormat(context)
                    ).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    stringResource(R.string.settings_time) + "  ·  " +
                        String.format(Locale.US, "%02d:%02d", hour, minute)
                )
            }
        }

        TextButton(
            onClick = { withPermission { NotificationHelper.showDailyHadith(context) } },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.settings_test))
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

        Text(
            text = stringResource(R.string.disclaimer),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
