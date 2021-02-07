package eu.javimar.mymoviesac.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import eu.javimar.mymoviesac.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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


@BindingAdapter("favorite")
fun FloatingActionButton.setFavorite(favorite: Boolean?) {
    val icon = if (favorite == true) R.drawable.ic_favorite_on
    else R.drawable.ic_favorite_off
    setImageDrawable(ContextCompat.getDrawable(context, icon))
}

@BindingAdapter("formatDate")
fun TextView.formatDate(date: String?)
{
    date?.let {
        val d = LocalDate.parse(it)
        val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
        text = d.format(formatter)
    }
}