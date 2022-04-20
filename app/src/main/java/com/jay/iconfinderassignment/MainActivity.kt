package com.jay.iconfinderassignment

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jay.iconfinderassignment.utils.TAG
import com.vmadalin.easypermissions.EasyPermissions
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var permissionHandler : PermissionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissionHandler = PermissionHandler.getInstance(this)
    }

    override fun onStart() {
        super.onStart()
        if(!permissionHandler.hasStoragePermission()) {
            showRequestPermissionDialog()
        }
    }

    private fun showRequestPermissionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Storage permission needed!")
        builder.setMessage("This app needs the storage permission to download icons!")
        builder.setCancelable(false)

        builder.setPositiveButton("Let's Go") { dialog, which ->
            permissionHandler.requestStoragePermission()
            dialog.dismiss()
        }

        builder.setNegativeButton("EXIT") { dialog, which ->
            this@MainActivity.finish()
            dialog.dismiss()
            exitProcess(0)
        }
        builder.show()
    }

    /*
    * Permission Callbacks
    * */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        permissionHandler.permissionDenied(perms)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Log.d(TAG, "Permission granted")
    }

}
