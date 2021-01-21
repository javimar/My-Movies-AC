package eu.javimar.mymoviesac.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.javimar.mymoviesac.model.Movie
import eu.javimar.mymoviesac.model.MovieDbResult
import eu.javimar.mymoviesac.model.MoviesApi
import kotlinx.coroutines.launch

enum class MovieApiStatus { LOADING, ERROR, DONE }

/**
 * The [ViewModel] that is attached to the [MovieListFragment]
 */
class MovieListingViewModel: ViewModel()
{

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<MovieApiStatus>()
    // The external immutable LiveData for the request status
    val status: LiveData<MovieApiStatus>
        get() = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of MarsProperty
    // with new values
    private val _movieResults = MutableLiveData<MovieDbResult>()
    // The external LiveData interface to the property is immutable, so only this class can modify
    val movies: LiveData<MovieDbResult>
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
            try {
                // TODO pasar API KEY al VM y la region
                _movieResults.value = MoviesApi.retrofitService.listPopularMoviesAsync(
                    "109c1631c7cc9e31de7e45027ec96985", "US")
                _status.value = MovieApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MovieApiStatus.ERROR
                _movieResults.value = null
            }
        }

        Log.e("JAVIER", "RESULTS= " + (_movieResults.value?.totalResults ?: 0))
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