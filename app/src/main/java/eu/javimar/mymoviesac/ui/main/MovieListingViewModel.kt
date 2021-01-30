package eu.javimar.mymoviesac.ui.main

import androidx.lifecycle.*
import eu.javimar.domain.Movie
import eu.javimar.usecases.GetPopularMovies
import kotlinx.coroutines.launch

enum class MovieApiStatus { LOADING, ERROR, DONE }

class MovieListingViewModel(private val getPopularMovies: GetPopularMovies) : ViewModel()
{
    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<MovieApiStatus>()
    val status: LiveData<MovieApiStatus>
        get() = _status

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = _movies

    private val _navigateToSelectedMovie = MutableLiveData<Int>()
    val navigateToSelectedMovie: LiveData<Int>
        get() = _navigateToSelectedMovie

    private val _requestLocationPermission = MutableLiveData<Unit>()
    val requestLocationPermission: LiveData<Unit>
        get() = _requestLocationPermission

    init {
        refresh()
    }

    private fun refresh() {
        _requestLocationPermission.value = Unit
    }

    fun onCoarsePermissionRequested()
    {
        viewModelScope.launch {
            _status.value = MovieApiStatus.LOADING
            try
            {
                _movies.value = getPopularMovies.invoke()
                _status.value = MovieApiStatus.DONE
            }
            catch (e: Exception)
            {
                _status.value = MovieApiStatus.ERROR
                _movies.value = null
            }
        }
    }


    fun displayMovieDetails(id: Int) {
        _navigateToSelectedMovie.value = id
    }

    // After the navigation has taken place, make sure is set to null
    fun displayMovieDetailsComplete() {
        _navigateToSelectedMovie.value = null
    }
}
