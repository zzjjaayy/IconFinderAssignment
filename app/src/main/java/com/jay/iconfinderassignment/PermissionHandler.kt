package com.jay.iconfinderassignment

import android.Manifest
import android.util.Log
import com.jay.iconfinderassignment.utils.TAG
import com.jay.iconfinderassignment.utils.sdk29AndUp
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class PermissionHandler (private val activity: MainActivity) {

    companion object{
        const val PERMISSION_STORAGE_REQUEST_CODE = 69
        val permission : String = Manifest.permission.WRITE_EXTERNAL_STORAGE

        private var INSTANCE : PermissionHandler? = null
        fun getInstance(activity: MainActivity) : PermissionHandler {
            if (INSTANCE ==null){
                INSTANCE = PermissionHandler(activity)
            }
            return INSTANCE!!
        }
    }

    fun hasStoragePermission() = EasyPermissions.hasPermissions(activity, permission)

    fun requestStoragePermission() {
        Log.d(TAG, "Requesting permission")
        EasyPermissions.requestPermissions(
            activity,
            activity.getString(R.string.permission_missing_message),
            PERMISSION_STORAGE_REQUEST_CODE,
            permission
        )
    }

    fun permissionDenied(perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(activity, perms)) {
            SettingsDialog.Builder(activity)
                .title(activity.getString(R.string.perm_steps_dialog_title))
                .positiveButtonText(activity.getString(R.string.settings))
                .negativeButtonText("")
                .rationale(activity.getString(R.string.steps_to_enable_perm))
                .build().show()
        } else {
            requestStoragePermission()
        }
    }
}