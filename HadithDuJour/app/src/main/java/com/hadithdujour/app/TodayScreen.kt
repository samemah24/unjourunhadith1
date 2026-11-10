package com.hadithdujour.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun TodayScreen(
    language: String,
    favorites: Set<Int>,
    onToggleFavorite: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val today = LocalDate.now()
    var offset by rememberSaveable { mutableIntStateOf(0) }

    val hadith = remember(offset, today) { HadithRepository.ofDay(context, today, offset) }
    val dateText = remember(today) {
        today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = dateText,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(if (offset == 0) R.string.today_title else R.string.discover_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(20.dp))

        HadithCard(hadith = hadith, language = language, large = true)

        HadithActions(
            isFavorite = hadith.id in favorites,
            onToggleFavorite = { onToggleFavorite(hadith.id) },
            onShare = { shareHadith(context, hadith, language) }
        )

        FilledTonalButton(
            onClick = { offset += 1 },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.action_another))
        }

        if (offset != 0) {
            TextButton(
                onClick = { offset = 0 },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.action_back_today))
            }
        }

        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.disclaimer),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
