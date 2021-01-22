package eu.javimar.mymoviesac.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.javimar.mymoviesac.network.Movie

class MovieDetailViewModelFactory (
    private val movie: Movie,
    private val application: Application) : ViewModelProvider.Factory
    {
        @Suppress("unchecked_cast")

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
                return MovieDetailViewModel(movie, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
