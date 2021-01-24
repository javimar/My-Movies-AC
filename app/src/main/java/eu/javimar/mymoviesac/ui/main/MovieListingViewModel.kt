package eu.javimar.mymoviesac.ui.main

import androidx.lifecycle.*
import eu.javimar.mymoviesac.model.server.Movie
import eu.javimar.mymoviesac.model.server.MovieDbResult
import eu.javimar.mymoviesac.model.server.MoviesRepository
import kotlinx.coroutines.launch

enum class MovieApiStatus { LOADING, ERROR, DONE }

class MovieListingViewModel(private val moviesRepository: MoviesRepository) : ViewModel()
{
    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<MovieApiStatus>()
    // The external immutable LiveData for the request status
    val status: LiveData<MovieApiStatus>
        get() = _status

    // Internally, we use a MutableLiveData, because we will be updating the list with new values
    private val _movieResults = MutableLiveData<MovieDbResult>()
    // The external LiveData interface to the property is immutable, so only this class can modify
    val movieResults: LiveData<MovieDbResult>
        get() = _movieResults

    // Internally, we use a MutableLiveData to handle navigation to the selected property
    private val _navigateToSelectedMovie = MutableLiveData<Movie>()
    // The external immutable LiveData for the navigation property
    val navigateToSelectedMovie: LiveData<Movie>
        get() = _navigateToSelectedMovie

    /**
     * Call getMovieListing() on init so we can display status immediately.
     */
    init {
        getMovieListing()
    }

    /**
     * The Retrofit service returns a coroutine Deferred, which we await to get the result of the transaction
     */
    private fun getMovieListing()
    {
        viewModelScope.launch {
            _status.value = MovieApiStatus.LOADING
            try
            {
                _movieResults.value = moviesRepository.findPopularMovies()
                _status.value = MovieApiStatus.DONE
            }
            catch (e: Exception)
            {
                _status.value = MovieApiStatus.ERROR
                _movieResults.value = null
            }
        }
    }


    /**
     * When the movie is clicked, set the [_navigateToSelectedMovie] [MutableLiveData]
     * @param movie The [Movie] that was clicked on.
     */
    fun displayMovieDetails(movie: Movie) {
        _navigateToSelectedMovie.value = movie
    }

    /**
     * After the navigation has taken place, make sure is set to null
     */
    fun displayMovieDetailsComplete() {
        _navigateToSelectedMovie.value = null
    }
}

class MovieListingViewModelFactory (private val repository: MoviesRepository) : ViewModelProvider.Factory
{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieListingViewModel::class.java))
        {
            return MovieListingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
