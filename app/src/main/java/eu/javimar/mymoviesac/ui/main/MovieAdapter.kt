package eu.javimar.mymoviesac.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.javimar.domain.Movie
import eu.javimar.mymoviesac.databinding.MoviesListItemBinding

class MovieAdapter (private val movieClickListener: MovieClickListener) :
        ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback())
{
    /**
     * Create new [RecyclerView] item views
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder
    {
        return MovieViewHolder.from(parent)
    }


    /**
     * Replaces the contents of a view
     */
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int)
    {
        holder.bind(getItem(position), movieClickListener)
    }


    /**
     * The MovieViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [Movie] information.
     */
    class MovieViewHolder private constructor(private val binding: MoviesListItemBinding):
        RecyclerView.ViewHolder(binding.root)
    {
        fun bind(movie: Movie, movieClickListener: MovieClickListener)
        {
            binding.movie = movie
            binding.clickListener = movieClickListener
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }

        companion object
        {
            fun from(parent: ViewGroup): MovieViewHolder
            {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MoviesListItemBinding.inflate(layoutInflater, parent, false)
                return MovieViewHolder(binding)
            }
        }
    }


    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>()
    {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean
        {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean
        {
            return oldItem == newItem
        }
    }


    // Custom listener that handles clicks on RecyclerView items
    class MovieClickListener(val clickListener: (id: Int) -> Unit)
    {
        fun onClick(movie: Movie) = clickListener(movie.id)
    }
}