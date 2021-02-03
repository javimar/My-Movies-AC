package eu.javimar.mymoviesac.ui.main

import android.util.Log
import androidx.lifecycle.*
import eu.javimar.domain.Movie
import eu.javimar.usecases.GetMovies
import kotlinx.coroutines.launch

enum class MovieApiStatus { LOADING, ERROR, DONE }

class MovieListingViewModel(private var sortBy: String,
                            private var year: String,
                            private val getMovies: GetMovies) : ViewModel()
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


    fun changeSortTypeAndYear(sort: String, year: String)
    {
        sortBy = sort
        this.year = year
        onCoarsePermissionRequested()
    }

    init {
        refresh()
    }

    private fun refresh() {
        _requestLocationPermission.value = Unit
    }

    fun onCoarsePermissionRequested()
    {

        //TODO
        Log.e("JAVIER", "SORT= " + sortBy)
        Log.e("JAVIER", "AÑO= " + year)


        viewModelScope.launch {
            _status.value = MovieApiStatus.LOADING
            try
            {
                _movies.value = getMovies.invoke(sortBy, year)
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
