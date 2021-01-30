package eu.javimar.mymoviesac.data

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import eu.javimar.data.repository.RegionRepository


class AndroidPermissionChecker(private val application: Application) :
    RegionRepository.PermissionChecker {

    override suspend fun check(permission: RegionRepository.PermissionChecker.Permission): Boolean =
        ContextCompat.checkSelfPermission(
            application,
            permission.toAndroidId()
        ) == PackageManager.PERMISSION_GRANTED
}

private fun RegionRepository.PermissionChecker.Permission.toAndroidId() = when (this) {
    RegionRepository.PermissionChecker.Permission.COARSE_LOCATION ->
        Manifest.permission.ACCESS_COARSE_LOCATION
}