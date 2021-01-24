package eu.javimar.mymoviesac.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.javimar.mymoviesac.model.server.Movie

class MovieDetailViewModelFactory (private val movie: Movie) : ViewModelProvider.Factory
    {
        @Suppress("unchecked_cast")

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
                return MovieDetailViewModel(movie) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
