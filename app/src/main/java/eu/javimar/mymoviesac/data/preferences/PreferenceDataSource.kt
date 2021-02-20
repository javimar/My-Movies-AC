package eu.javimar.mymoviesac.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import eu.javimar.data.source.InternalDataSource
import eu.javimar.mymoviesac.R

class PreferenceDataSource(val context: Context) : InternalDataSource
{
    private val prefs: SharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(context)

    override fun getLanguageData(): String
    {
        return prefs.getString(context.getString(R.string.pref_language_key), "en-US")!!
    }
}