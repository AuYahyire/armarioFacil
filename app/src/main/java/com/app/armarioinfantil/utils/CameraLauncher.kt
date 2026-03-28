package com.app.armarioinfantil.utils

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Helper composable that sets up a camera launcher.
 *
 * @param onPhotoCaptured Callback triggered when the photo is successfully captured.
 * Passes the String representation of the Uri (to be stored in the database).
 */
@Composable
fun rememberCameraLauncher(onPhotoCaptured: (String) -> Unit): () -> Unit {
    val context = LocalContext.current

    // Hold the temporary Uri where the camera will save the image
    val tempUri = remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempUri.value?.let { uri ->
                onPhotoCaptured(uri.toString())
            }
        }
    }

    // Return a function that can be called to launch the camera
    return {
        val file = PhotoUtils.createImageFile(context)
        val uri = PhotoUtils.getUriForFile(context, file)
        tempUri.value = uri
        cameraLauncher.launch(uri)
    }
}
