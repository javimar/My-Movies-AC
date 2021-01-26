package eu.javimar.mymoviesac.ui.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.javimar.mymoviesac.PermissionRequester
import eu.javimar.mymoviesac.R
import eu.javimar.mymoviesac.common.app
import eu.javimar.mymoviesac.common.getViewModel
import eu.javimar.mymoviesac.databinding.FragmentMovieListingBinding
import eu.javimar.mymoviesac.model.server.MoviesRepository


class MovieListFragment: Fragment()
{
    private lateinit var viewModel: MovieListingViewModel
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

        viewModel = getViewModel { MovieListingViewModel(MoviesRepository(app)) }

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
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {

        return true
    }

}