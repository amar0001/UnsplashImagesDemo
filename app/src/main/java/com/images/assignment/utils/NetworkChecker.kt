package com.images.assignment.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
    connected: @Composable () -> Unit,
    notConnected: @Composable () -> Unit,
    showDialog: Boolean
) {
    val context = LocalContext.current
    var openDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
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

    if (!connectedToInternet.value && showDialog) {
        AlertDialog(onDismissRequest = {}, title = { Text("No Internet Connection") }, text = {
            Text("Please connect to the internet and try again.")
        }, confirmButton = {
            Button(onClick = { /*TODO*/ }) {
                Text("Retry")
            }
        })

        notConnected()
    } else {
        connected() // Here you can call your API
    }
}