package eu.javimar.mymoviesac.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.javimar.mymoviesac.databinding.FragmentMovieDetailBinding

class MovieDetailFragment: Fragment()
{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        val binding = FragmentMovieDetailBinding.inflate(inflater)


        setHasOptionsMenu(true)
        return binding.root
    }

}