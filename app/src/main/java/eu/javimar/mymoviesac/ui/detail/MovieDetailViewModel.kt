package eu.javimar.mymoviesac.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.javimar.mymoviesac.model.database.Movie
import eu.javimar.mymoviesac.model.server.MoviesRepository
import kotlinx.coroutines.launch

class MovieDetailViewModel(private val movieId: Int,
                           private val moviesRepository: MoviesRepository) : ViewModel()
{
    private val _selectedMovie = MutableLiveData<Movie>()
    val selectedMovie: LiveData<Movie>
        get() = _selectedMovie

    private val _favorite = MutableLiveData<Boolean>()
    val favorite: LiveData<Boolean>
        get() = _favorite



    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    // Initialize the _selectedMovie MutableLiveData
    init {
        viewModelScope.launch {
            _selectedMovie.value = moviesRepository.findMovieById(movieId)
            updateUi()
        }
    }

    fun onFavoriteClicked()
    {
        viewModelScope.launch {
            selectedMovie.value?.let {
                val updatedMovie = it.copy(favorite = !it.favorite)
                _selectedMovie.value = updatedMovie
                updateUi()
                moviesRepository.updateMovie(updatedMovie)
            }
        }
    }

    private fun updateUi()
    {
        selectedMovie.value?.run {
            _title.value = title
            _favorite.value = favorite
        }
    }
}