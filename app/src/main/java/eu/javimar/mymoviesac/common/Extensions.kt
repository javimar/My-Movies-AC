package eu.javimar.mymoviesac.common

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import eu.javimar.mymoviesac.MoviesApp


@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> Fragment.getViewModel(crossinline factory: () -> T): T
{
    val vmFactory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }

    return ViewModelProvider(this, vmFactory).get()
}


val Context.app: MoviesApp
    get() = applicationContext as MoviesApp

val Fragment.app: MoviesApp
    get() = ((activity?.app)
        ?: IllegalStateException("Fragment needs to be attached to the activity in order to access the App instance"))
            as MoviesApp