package com.hadithdujour.app

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HadithCard(
    hadith: Hadith,
    language: String,
    large: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(if (large) 24.dp else 18.dp)) {
            SelectionContainer {
                Text(
                    text = hadith.text(language),
                    fontSize = if (large) 21.sp else 17.sp,
                    lineHeight = if (large) 32.sp else 25.sp
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.narrated_by, hadith.narrator),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = stringResource(R.string.source_format, hadith.source),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.alpha(0.8f)
            )
        }
    }
}

@Composable
fun HadithActions(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onToggleFavorite) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = stringResource(
                    if (isFavorite) R.string.action_unfavorite else R.string.action_favorite
                ),
                tint = if (isFavorite) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
        IconButton(onClick = onShare) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = stringResource(R.string.action_share),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

fun shareHadith(context: Context, hadith: Hadith, language: String) {
    val body = hadith.text(language) + "\n\n— " + hadith.narrator + " (" + hadith.source + ")"
    val send = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, body)
    }
    context.startActivity(Intent.createChooser(send, context.getString(R.string.action_share)))
}
