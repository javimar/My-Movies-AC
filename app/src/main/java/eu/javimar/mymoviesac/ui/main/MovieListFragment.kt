package eu.javimar.mymoviesac.ui.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import eu.javimar.mymoviesac.R
import eu.javimar.mymoviesac.common.*
import eu.javimar.mymoviesac.databinding.FragmentMovieListingBinding
import eu.javimar.mymoviesac.ui.main.MovieListingViewModel.UIModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate

class MovieListFragment: ScopeFragment()
{
    private val releaseDateGte = getOneMonthBefore()
    private val releaseDateLte = getTodayFormattedForQuery(LocalDate.now())
    private val sortBy = SORT_BY_POPULARITY
    private var isPopular = true

    private lateinit var adapter: MovieAdapter

    private val viewModel: MovieListingViewModel by viewModel {
        parametersOf(sortBy, releaseDateGte, releaseDateLte, isPopular)
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

        adapter = MovieAdapter(MovieAdapter.MovieClickListener {
            viewModel.onMovieClicked(it)
        })
        binding.recyclerViewMovies.adapter = adapter

        binding.apply {
            viewModel = viewModel
            lifecycleOwner = this@MovieListFragment
        }

        viewModel.status.observe(viewLifecycleOwner, Observer(::updateUi))

        requireActivity().toolbar.visibility = View.VISIBLE

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
                setTabListener()
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

    private fun setTabListener()
    {
        val navController = requireActivity().findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id)
            {
                R.id.movieListFragmentPopular -> {
                    isPopular = true
                    viewModel.showMovies(isPopular)
                }
                R.id.movieListFragmentNewMovies -> {
                    isPopular = false
                    viewModel.showMovies(isPopular)
                }
            }
        }
    }

    companion object {
        const val SORT_BY_POPULARITY = "popularity.desc"
    }
}