package eu.javimar.mymoviesac.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import eu.javimar.mymoviesac.databinding.FragmentMovieDetailBinding

class MovieDetailFragment: Fragment()
{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        val binding = FragmentMovieDetailBinding.inflate(inflater)

        binding.lifecycleOwner = this

        val movie = MovieDetailFragmentArgs.fromBundle(requireArguments()).movie

        val viewModelFactory = MovieDetailViewModelFactory(movie)
        binding.viewModel = ViewModelProvider(this, viewModelFactory).get(MovieDetailViewModel::class.java)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = movie.title

        return binding.root
    }

}