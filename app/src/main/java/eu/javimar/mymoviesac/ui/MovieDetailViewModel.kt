package eu.javimar.mymoviesac.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.javimar.mymoviesac.network.Movie

class MovieDetailViewModel(movie: Movie, app: Application) : AndroidViewModel(app)
{
    private val _selectedMovie = MutableLiveData<Movie>()

    // The external LiveData for the SelectedMovie
    val selectedMovie: LiveData<Movie>
        get() = _selectedMovie

    // Initialize the _selectedMovie MutableLiveData
    init {
        _selectedMovie.value = movie
    }

}