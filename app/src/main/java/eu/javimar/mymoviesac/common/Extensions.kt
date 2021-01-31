package eu.javimar.mymoviesac.common

import android.content.Context
import androidx.fragment.app.Fragment
import eu.javimar.mymoviesac.MoviesApp

val Context.app: MoviesApp
    get() = applicationContext as MoviesApp

val Fragment.app: MoviesApp
    get() = ((activity?.app)
        ?: IllegalStateException("Fragment needs to be attached to the activity in order to access the App instance"))
            as MoviesApp