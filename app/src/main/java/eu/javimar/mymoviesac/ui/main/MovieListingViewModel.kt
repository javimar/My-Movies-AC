package eu.javimar.mymoviesac.ui.main

import androidx.lifecycle.*
import eu.javimar.domain.Movie
import eu.javimar.usecases.GetMovies
import kotlinx.coroutines.launch

class MovieListingViewModel(private var sortBy: String,
                            private var isPopular: Boolean,
                            private val refreshMovies: GetMovies) : ViewModel()
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
