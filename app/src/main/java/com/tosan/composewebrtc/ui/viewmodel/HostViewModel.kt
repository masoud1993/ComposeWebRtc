package com.tosan.composewebrtc.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tosan.composewebrtc.remote.socket.server.SocketServer
import com.tosan.composewebrtc.remote.socket.server.SocketServerListener
import com.tosan.composewebrtc.utils.MessageModel
import com.tosan.composewebrtc.utils.getWifiIPAddress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HostViewModel @Inject constructor(
    private val application: Application,
    private val socketServer: SocketServer
) : ViewModel(), SocketServerListener {

    private var ipAddress: String? = null

    val hostAddressState: MutableStateFlow<String?> = MutableStateFlow(null)

    fun init(done: (Boolean) -> Unit){
        ipAddress = getWifiIPAddress(application)
        if (ipAddress == null) {
            done(false)
            return
        }

        startSocketServer()
    }

    override fun onCleared() {
        super.onCleared()
        socketServer.onDestroy()
    }

    private fun startSocketServer() {
        socketServer.init(this@HostViewModel)
    }

    override fun onSocketServerNewMessage(message: MessageModel) {

    }

    override fun onStartServer(port: Int) {
        viewModelScope.launch {
            hostAddressState.emit("Host Address : $ipAddress:$port")
        }
    }

    override fun onClientDisconnected() {

    }

}