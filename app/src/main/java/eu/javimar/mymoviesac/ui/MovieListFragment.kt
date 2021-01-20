package eu.javimar.mymoviesac.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import eu.javimar.mymoviesac.R
import eu.javimar.mymoviesac.databinding.FragmentMovieListingBinding

class MovieListFragment: Fragment()
{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val binding = FragmentMovieListingBinding.inflate(inflater)


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