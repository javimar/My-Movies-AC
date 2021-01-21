package eu.javimar.mymoviesac.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.javimar.mymoviesac.databinding.MoviesListItemBinding
import eu.javimar.mymoviesac.model.Movie

class MovieAdapter (val onClickListener: OnClickListener ) :
ListAdapter<Movie, MovieAdapter.MovieViewHolder>(DiffCallback)
{
    /**
     * The MovieViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [Movie] information.
     */
    class MovieViewHolder(private var binding: MoviesListItemBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(movie: Movie)
        {
            binding.movie = movie
            //binding.movieTitle.text = movie.title
           // binding.moviePosterView.loadUrl("https://image.tmdb.org/t/p/w185/${movie.posterPath}")

            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Movie]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Movie>()
    {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder
    {
        return MovieViewHolder(MoviesListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int)
    {
        val movie = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(movie)
        }
        holder.bind(movie)
    }


    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Movie]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Movie]
     */
    class OnClickListener(val clickListener: (movie: Movie) -> Unit)
    {
        fun onClick(movie: Movie) = clickListener(movie)
    }
}