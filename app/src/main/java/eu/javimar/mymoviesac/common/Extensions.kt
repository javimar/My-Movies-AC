package eu.javimar.mymoviesac.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.fragment.app.Fragment
import eu.javimar.mymoviesac.MoviesApp

val Context.app: MoviesApp
    get() = applicationContext as MoviesApp

val Fragment.app: MoviesApp
    get() = ((activity?.app)
        ?: IllegalStateException("Fragment needs to be attached to the activity in order to access the App instance"))
            as MoviesApp

val Context.isConnected: Boolean
    get()
    {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
            {
                val nw = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
                when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
            else ->
            {
                // Use depreciated methods only on older devices
                val nwInfo = connectivityManager.activeNetworkInfo ?: return false
                nwInfo.isConnected
            }
        }
    }