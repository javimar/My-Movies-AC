package eu.javimar.mymoviesac.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.javimar.domain.Movie
import eu.javimar.usecases.FindMovieById
import eu.javimar.usecases.ToggleMovieFavorite
import kotlinx.coroutines.launch

class MovieDetailViewModel(private val movieId: Int,
                           private val findMovieById: FindMovieById,
                           private val toggleFavorite: ToggleMovieFavorite) : ViewModel()
{
    private val _selectedMovie = MutableLiveData<Movie>()
    val selectedMovie: LiveData<Movie>
        get() = _selectedMovie

    // Initialize the _selectedMovie MutableLiveData
    init {
        viewModelScope.launch {
            _selectedMovie.value = findMovieById(movieId)
        }
    }

    fun onFavoriteClicked()
    {
        viewModelScope.launch {
            selectedMovie.value?.let {
                val updatedMovie = it.copy(favorite = !it.favorite)
                _selectedMovie.value = updatedMovie
                toggleFavorite.invoke(it)
            }
        }
    }
}