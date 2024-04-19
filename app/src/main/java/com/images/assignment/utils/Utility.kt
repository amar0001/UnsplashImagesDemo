package com.images.assignment.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.material.Text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun showInternetAlert(context: Context) {
    showAlertDialog(
        context = context,
        "No Internet Connection",
        "Please connect to the internet and try again."
    )
}

suspend fun showAlertDialog(context: Context, title: String, message: String) {
    withContext(Dispatchers.Main) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                // Do something when the "OK" button is clicked
                dialog.dismiss()
            }
            .show()
    }
}

fun Context.toast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

suspend fun showToast(context: Context, message: String) {
    withContext(Dispatchers.Main) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}