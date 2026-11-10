package com.hadithdujour.app

import android.content.Context
import org.json.JSONArray
import java.time.LocalDate

data class Hadith(
    val id: Int,
    val narrator: String,
    val source: String,
    val texts: Map<String, String>
) {
    fun text(language: String): String = texts[language] ?: texts["en"] ?: ""
}

object HadithRepository {

    val supportedLanguages = listOf("fr", "en", "de", "es")

    @Volatile
    private var cache: List<Hadith>? = null

    fun all(context: Context): List<Hadith> {
        cache?.let { return it }
        val json = context.applicationContext.assets
            .open("hadiths.json")
            .bufferedReader(Charsets.UTF_8)
            .use { it.readText() }
        val array = JSONArray(json)
        val list = buildList {
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val text = obj.getJSONObject("text")
                val texts = supportedLanguages
                    .associateWith { text.optString(it) }
                    .filterValues { it.isNotBlank() }
                add(
                    Hadith(
                        id = obj.getInt("id"),
                        narrator = obj.getString("narrator"),
                        source = obj.getString("source"),
                        texts = texts
                    )
                )
            }
        }
        cache = list
        return list
    }

    /** Le hadith du jour : change chaque jour, identique pour toute la journée. */
    fun ofDay(context: Context, date: LocalDate = LocalDate.now(), offset: Int = 0): Hadith {
        val list = all(context)
        val index = (date.toEpochDay() + offset).mod(list.size.toLong()).toInt()
        return list[index]
    }
}
