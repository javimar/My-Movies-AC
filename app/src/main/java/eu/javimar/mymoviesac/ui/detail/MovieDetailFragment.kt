package eu.javimar.mymoviesac.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import eu.javimar.mymoviesac.R
import eu.javimar.mymoviesac.databinding.FragmentMovieDetailBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.scope.ScopeFragment
import org.koin.core.parameter.parametersOf
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailFragment: ScopeFragment()
{
    private val args: MovieDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentMovieDetailBinding

    private val viewModel: MovieDetailViewModel by viewModel {
        parametersOf(args.id)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_movie_detail, container,false)

        requireActivity().toolbar.visibility = View.GONE
        (activity as AppCompatActivity).setSupportActionBar(binding.movieDetailToolbar)
        NavigationUI.setupActionBarWithNavController((activity as AppCompatActivity), findNavController())

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewmodel = viewModel
            lifecycleOwner = this@MovieDetailFragment
        }
    }
}