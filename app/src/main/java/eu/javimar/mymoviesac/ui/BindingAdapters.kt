package eu.javimar.mymoviesac.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import eu.javimar.mymoviesac.R
import eu.javimar.mymoviesac.model.database.Movie
import eu.javimar.mymoviesac.model.server.MovieDbResult
import eu.javimar.mymoviesac.ui.main.MovieAdapter
import eu.javimar.mymoviesac.ui.main.MovieApiStatus


/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?)
{
    imgUrl?.let {
        Glide.with(imgView.context)
            .load(it)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}

/**
 * This binding adapter displays the [MovieApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */
@BindingAdapter("movieApiStatus")
fun bindStatus(statusImageView: ImageView, status: MovieApiStatus?)
{
    when (status) {
        MovieApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        MovieApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        MovieApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

@BindingAdapter("favorite")
fun FloatingActionButton.setFavorite(favorite: Boolean?) {
    val icon = if (favorite == true) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
    setImageDrawable(ContextCompat.getDrawable(context, icon))
}
