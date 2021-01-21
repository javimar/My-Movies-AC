package eu.javimar.mymoviesac.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import eu.javimar.mymoviesac.R
import eu.javimar.mymoviesac.databinding.FragmentMovieListingBinding

class MovieListFragment: Fragment()
{
    private val viewModel: MovieListingViewModel by lazy {
        ViewModelProvider(this).get(MovieListingViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        val binding = FragmentMovieListingBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel


        binding.recyclerViewMovies.adapter = MovieAdapter(MovieAdapter.OnClickListener {
            viewModel.displayMovieDetails(it)
        })

/*
        // Observe the navigateToSelectedMovie LiveData and Navigate when it isn't null
        // After navigating, call displayMovieDetailsComplete() so that the ViewModel is ready
        // for another navigation event.
        viewModel.navigateToSelectedMovie.observe(viewLifecycleOwner, Observer { movie ->
            movie?.let {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment))
                // Signal navigation ended
                viewModel.displayMovieDetailsComplete()
            }
        })
*/

        setHasOptionsMenu(true)
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