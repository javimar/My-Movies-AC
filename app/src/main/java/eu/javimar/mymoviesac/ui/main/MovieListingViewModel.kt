package eu.javimar.mymoviesac.ui.main

import androidx.lifecycle.*
import eu.javimar.domain.Movie
import eu.javimar.usecases.GetFavMovies
import eu.javimar.usecases.GetMovies
import eu.javimar.usecases.ReloadMoviesFromServer
import kotlinx.coroutines.launch

class MovieListingViewModel(private var sortBy: String,
                            private var isPopular: Boolean,
                            private val refreshMovies: GetMovies,
                            private val getFavMovies: GetFavMovies,
                            private val reloadMovies: ReloadMoviesFromServer) : ViewModel()
{
    private val _status = MutableLiveData<UIModel>()
    val status: LiveData<UIModel>
        get() {
            if(_status.value == null)  refresh()
            return _status
        }

    sealed class UIModel
    {
        object Loading : UIModel()
        data class Loaded(val movies: List<Movie>) : UIModel()
        data class Navigated(val movieId: Int) : UIModel()
        object RequestLocationPermission : UIModel()
        object Error : UIModel()
    }


    private fun refresh()
    {
        _status.value = UIModel.RequestLocationPermission
    }

    fun showMovies(isPopular: Boolean)
    {
        this.isPopular = isPopular
        onCoarsePermissionRequested()
    }


    fun showFavMovies()
    {
        viewModelScope.launch {
            _status.value = UIModel.Loaded(getFavMovies.invoke())
        }
    }

    fun reloadMoviesFromServer()
    {
        viewModelScope.launch {
            _status.value = UIModel.Loading
            try
            {
                reloadMovies.invoke(sortBy)
                _status.value = UIModel.Loaded(refreshMovies
                    .invoke(sortBy, isPopular))
            }
            catch (e: Exception)
            {
                _status.value = UIModel.Error
            }
        }
    }

    fun onCoarsePermissionRequested()
    {
        viewModelScope.launch {
            _status.value = UIModel.Loading
            try
            {
                _status.value = UIModel.Loaded(refreshMovies
                    .invoke(sortBy, isPopular))
            }
            catch (e: Exception)
            {
                _status.value = UIModel.Error
            }
        }
    }

    fun onMovieClicked(movieId: Int)
    {
        _status.value = UIModel.Navigated(movieId)
    }

    fun onMovieNavigated()
    {
        refresh()
    }
}
