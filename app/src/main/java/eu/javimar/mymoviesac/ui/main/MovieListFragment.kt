package eu.javimar.mymoviesac.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import eu.javimar.mymoviesac.R
import eu.javimar.mymoviesac.databinding.FragmentMovieListingBinding
import eu.javimar.mymoviesac.model.server.MoviesRepository


class MovieListFragment: Fragment()
{
    private lateinit var viewModel: MovieListingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        setHasOptionsMenu(true)

        val binding = FragmentMovieListingBinding.inflate(inflater)
        val application = requireNotNull(activity).application

        val viewModelFactory = MovieListingViewModelFactory(MoviesRepository(application))
        viewModel = ViewModelProvider(this, viewModelFactory).get(MovieListingViewModel::class.java)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.recyclerViewMovies.adapter = MovieAdapter(MovieAdapter.OnClickListener {
            viewModel.displayMovieDetails(it)
        })


        // Observe the navigateToSelectedMovie LiveData and Navigate when it isn't null
        // After navigating, call displayMovieDetailsComplete() so that the ViewModel is ready
        // for another navigation event.
        viewModel.navigateToSelectedMovie.observe(viewLifecycleOwner, { movie ->
            movie?.let {
                // Find the NavController from the Fragment
                this.findNavController().navigate(MovieListFragmentDirections
                    .actionMovieListFragmentToMovieDetailFragment(it))
                // Signal navigation ended
                viewModel.displayMovieDetailsComplete()
            }
        })

        return binding.root
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