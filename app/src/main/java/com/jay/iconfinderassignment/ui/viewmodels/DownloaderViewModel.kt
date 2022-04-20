package com.jay.iconfinderassignment.ui.viewmodels

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.jay.iconfinderassignment.data.model.Icon
import com.jay.iconfinderassignment.utils.AUTH_TOKEN
import com.jay.iconfinderassignment.utils.TAG
import java.io.File

class DownloaderViewModel(application: Application) : AndroidViewModel(application){

    fun downloadMedia(context: Context, icon: Icon){
        Log.d(TAG, "downloadMedia: downloading")
        // Setting up the target Directory ->
        val directory = File(Environment.DIRECTORY_DOWNLOADS)
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val highestFormat = icon.raster_sizes[icon.raster_sizes.size - 1].formats[0]
        val downloadUri = Uri.parse(highestFormat.download_url) // parse url to uri
        // Creating a request with all necessary methods for the details
        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(icon.icon_id.toString())
                .setDescription("")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                // Notifications for DownloadManager are optional and can be removed
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    icon.icon_id.toString() + "." + highestFormat.format
                )
                .addRequestHeader("Authorization", AUTH_TOKEN)
        }
        Log.d(TAG, "downloadMedia: ${icon.icon_id.toString() + "." + highestFormat.format}")
        Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show()
        (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
    }
}