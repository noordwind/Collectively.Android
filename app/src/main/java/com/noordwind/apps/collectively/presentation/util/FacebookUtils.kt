package com.noordwind.apps.collectively.presentation.util

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

object FacebookUtils {
    fun newFacebookIntent(pm: PackageManager, url: String): Intent {
        var uri: Uri
        try {
            pm.getPackageInfo("com.facebook.katana", 0)
            // http://stackoverflow.com/a/24547437/1048340
            uri = Uri.parse("fb://facewebmodal/f?href=" + url)
        } catch (e: PackageManager.NameNotFoundException) {
            uri = Uri.parse(url)
        }

        return Intent(Intent.ACTION_VIEW, uri)
    }
}
