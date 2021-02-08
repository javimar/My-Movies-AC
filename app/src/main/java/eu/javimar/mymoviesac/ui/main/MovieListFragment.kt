package eu.javimar.mymoviesac.ui.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import eu.javimar.mymoviesac.R
import eu.javimar.mymoviesac.common.*
import eu.javimar.mymoviesac.databinding.FragmentMovieListingBinding
import eu.javimar.mymoviesac.ui.main.MovieListingViewModel.UIModel
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate

class MovieListFragment: ScopeFragment()
{
    private val releaseDateGte = getOneMonthBefore()
    private val releaseDateLte = getTodayFormattedForQuery(LocalDate.now())
    private var sortBy = SORT_BY_POPULARITY
    private var isPopular = true

    private lateinit var adapter: MovieAdapter

    private val viewModel: MovieListingViewModel by viewModel {
        parametersOf(sortBy, "", "", isPopular)
    }
    private val coarsePermissionRequester by lazy {
        PermissionRequester(requireActivity(), ACCESS_COARSE_LOCATION)
    }
    private lateinit var binding: FragmentMovieListingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_movie_listing, container,false)

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(binding.mainBar.toolbar)
        binding.mainBar.toolbar.setTitle(R.string.title_popular_movies)


        adapter = MovieAdapter(MovieAdapter.MovieClickListener {
            viewModel.onMovieClicked(it)
        })
        binding.recyclerViewMovies.adapter = adapter

        binding.apply {
            viewModel = viewModel
            lifecycleOwner = this@MovieListFragment
        }

        viewModel.status.observe(viewLifecycleOwner, Observer(::updateUi))

        return binding.root
    }


    private fun updateUi(status: UIModel)
    {
        when (status)
        {
            is UIModel.Loading -> binding.statusImage.setImageResource(R.drawable.loading_animation)

            is UIModel.Loaded -> {
                binding.statusImage.visibility = GONE
                adapter.submitList(status.movies)
            }

            is UIModel.Navigated ->  {
                this.findNavController().navigate(MovieListFragmentDirections
                    .actionMovieListFragmentToMovieDetailFragment().setId(status.movieId))
                viewModel.onMovieNavigated()
            }

            // First entry point when accepting permission
            is UIModel.RequestLocationPermission -> coarsePermissionRequester.request {
                viewModel.onCoarsePermissionRequested()
            }
            is UIModel.Error -> {
                if(requireActivity().isConnected)
                {
                    requireActivity().showError(R.string.err_server, Toast.LENGTH_SHORT)
                }
                else
                {
                    binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                    requireActivity().showError(R.string.err_nointernet, Toast.LENGTH_SHORT)
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId)
        {
            R.id.action_popular -> {
                binding.mainBar.toolbar.setTitle(R.string.title_popular_movies)
                // Navigate to most popular
                sortBy = SORT_BY_POPULARITY
                isPopular = true
                viewModel.changeSortTypeAndYear(sortBy, "", "", isPopular)
                true
            }
            R.id.action_new -> {
                // Navigate to new movies playing
                sortBy = ""
                isPopular = false
                binding.mainBar.toolbar.title = getString(R.string.title_new_movies)
                viewModel.changeSortTypeAndYear(sortBy, releaseDateGte, releaseDateLte, isPopular)
                true
            }
            R.id.action_settings -> {
                findNavController().navigate(R.id.open_settings_fragment)
                true
            }
            else -> true
        }
    }

    companion object {
        //private const val SORT_BY_YEAR = "release_date.desc"
        private const val SORT_BY_POPULARITY = "popularity.desc"
    }
}