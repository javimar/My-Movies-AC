package eu.javimar.mymoviesac.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import eu.javimar.mymoviesac.R

class Settings : PreferenceFragmentCompat()
{

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?)
    {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}