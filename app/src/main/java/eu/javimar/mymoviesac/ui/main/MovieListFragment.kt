package eu.javimar.mymoviesac.ui.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import eu.javimar.mymoviesac.common.PermissionRequester
import eu.javimar.mymoviesac.R
import eu.javimar.mymoviesac.databinding.FragmentMovieListingBinding
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate

class MovieListFragment: ScopeFragment()
{
    private var year: String = ""
    private var sortBy: String = SORT_BY_POPULARITY

    private val viewModel: MovieListingViewModel by viewModel {
        parametersOf(sortBy, year)
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

        viewModel.requestLocationPermission.observe(viewLifecycleOwner, {
            coarsePermissionRequester.request {
                viewModel.onCoarsePermissionRequested()
            }
        })

        val adapter = MovieAdapter(MovieAdapter.MovieClickListener {
            viewModel.displayMovieDetails(it)
        })
        binding.recyclerViewMovies.adapter = adapter

        binding.apply {
            viewModel = viewModel
            lifecycleOwner = this@MovieListFragment
        }

        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: MovieAdapter)
    {
        viewModel.navigateToSelectedMovie.observe(viewLifecycleOwner, { id ->
            id?.let {
                // Find the NavController from the Fragment
                this.findNavController().navigate(MovieListFragmentDirections
                    .actionMovieListFragmentToMovieDetailFragment().setId(it))
                // Signal navigation ended
                viewModel.displayMovieDetailsComplete()
            }
        })

        // Observe List of movies from ViewModel and refresh if changes occur
        viewModel.movies.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId) {
            R.id.action_popular -> {
                binding.mainBar.toolbar.setTitle(R.string.title_popular_movies)
                // Navigate to most populat
                sortBy = SORT_BY_POPULARITY
                year = ""
                viewModel.changeSortTypeAndYear(sortBy, year)
                true
            }
            R.id.action_new -> {
                // Navigate to new movies
                sortBy = SORT_BY_YEAR
                year = LocalDate.now().year.toString()
                binding.mainBar.toolbar.title = String.format(getString(R.string.title_new_movies), year)
                viewModel.changeSortTypeAndYear(sortBy, year)
                true
            }
            R.id.action_settings -> {
                findNavController().navigate(R.id.open_settings_fragment)
                true
            }
            else -> {
                sortBy = SORT_BY_POPULARITY
                true
            }
        }
    }






    companion object {
        private const val SORT_BY_YEAR = "release_date.desc"
        private const val SORT_BY_POPULARITY = "popularity.desc"
    }
}