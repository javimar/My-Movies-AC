package eu.javimar.mymoviesac.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import eu.javimar.mymoviesac.R
import eu.javimar.mymoviesac.common.app
import eu.javimar.mymoviesac.common.getViewModel
import eu.javimar.mymoviesac.databinding.FragmentMovieDetailBinding
import eu.javimar.mymoviesac.model.server.MoviesRepository

class MovieDetailFragment: Fragment()
{
    private val args: MovieDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentMovieDetailBinding
    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_movie_detail, container,false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel { MovieDetailViewModel(args.id, MoviesRepository(app)) }

        binding.apply {
            viewModel = viewModel
            lifecycleOwner = this@MovieDetailFragment
        }
    }
}