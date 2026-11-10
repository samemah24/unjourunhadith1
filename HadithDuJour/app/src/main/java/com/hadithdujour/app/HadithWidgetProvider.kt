package com.hadithdujour.app

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class HadithWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val views = buildViews(context)
        appWidgetIds.forEach { id -> appWidgetManager.updateAppWidget(id, views) }
    }

    companion object {

        fun buildViews(context: Context): RemoteViews {
            val hadith = HadithRepository.ofDay(context)
            val language = Prefs(context).effectiveLanguage()

            val views = RemoteViews(context.packageName, R.layout.widget_hadith)
            views.setTextViewText(R.id.widget_title, context.getString(R.string.notif_title))
            views.setTextViewText(R.id.widget_text, hadith.text(language))
            views.setTextViewText(R.id.widget_source, hadith.source)

            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)
            return views
        }

        fun updateAll(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, HadithWidgetProvider::class.java))
            if (ids.isEmpty()) return
            val views = buildViews(context)
            ids.forEach { id -> manager.updateAppWidget(id, views) }
        }
    }
}
