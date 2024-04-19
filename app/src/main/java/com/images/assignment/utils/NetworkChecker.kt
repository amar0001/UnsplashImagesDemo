package com.images.assignment.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun NetworkChecker(connected: @Composable () -> Unit, notConnected: @Composable () -> Unit) {
    NetworkChecker(connected, notConnected, false)
}

@Composable
fun NetworkChecker(
    connected: @Composable  () -> Unit,
    notConnected: @Composable () -> Unit,
    showDialog: Boolean
) {
    val context = LocalContext.current

    var _showDialog by remember { mutableStateOf(showDialog) }
    var offlineLoad by remember { mutableStateOf(false) }

    val connectedToInternet = checkInternet(context)

    if (!connectedToInternet.value && showDialog) {


        if(_showDialog) {
            AlertDialog(onDismissRequest = {
                _showDialog = false
            }, title = { Text("No Internet Connection") }, text = {
                Text("Please connect to the internet and try again.")
            }, confirmButton = {
                Button(onClick = {
                    _showDialog = false

                }) {
                    Text("OK")
                }
            })
            notConnected()
        }
    } else {
        connected() // Here you can call your API
    }


}

@Composable
 public fun checkInternet(context: Context): State<Boolean> {
    val connectivityManager = remember {
        context.getSystemService(ConnectivityManager::class.java)
    }
    val wifiLazy = connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)
        ?.hasTransport(
            NetworkCapabilities.TRANSPORT_WIFI
        ) ?: false
    val mobileDataLazy =
        connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
            ) ?: false
    val connectedToInternet = remember {
        derivedStateOf {
            wifiLazy || mobileDataLazy
        }
    }
    return connectedToInternet
}


fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}