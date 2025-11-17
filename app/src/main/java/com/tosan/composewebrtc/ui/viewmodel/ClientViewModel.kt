package com.tosan.composewebrtc.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tosan.composewebrtc.remote.socket.client.SocketClient
import com.tosan.composewebrtc.remote.socket.client.SocketClientListener
import com.tosan.composewebrtc.utils.MessageModel
import com.tosan.composewebrtc.utils.MessageModelType
import com.tosan.composewebrtc.webrtc.PeerConnectionObserver
import com.tosan.composewebrtc.webrtc.RTCClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.SessionDescription
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ClientViewModel @Inject constructor(
    private val application: Application,
    private val socketClient: SocketClient,
    private val gson: Gson
) : ViewModel(), SocketClientListener {

    private var remoteView: SurfaceViewRenderer? = null
    val callDisconnected: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val rtcClient: RTCClient by lazy {
        RTCClient(
            application, object : PeerConnectionObserver() {
                override fun onIceCandidate(p0: IceCandidate?) {
                    super.onIceCandidate(p0)
                    rtcClient.addIceCandidate(p0)
                    socketClient.sendDataToHost(MessageModel(MessageModelType.ICE, gson.toJson(p0)))
                }

                override fun onAddStream(p0: MediaStream?) {
                    super.onAddStream(p0)
                    remoteView?.let { remote ->
                        p0?.let { mediaStream ->
                            mediaStream.videoTracks[0]?.addSink(remote)
                        }
                    }
                }


                override fun onConnectionChange(newState: PeerConnection.PeerConnectionState?) {
                    super.onConnectionChange(newState)

                    if (newState == PeerConnection.PeerConnectionState.DISCONNECTED ||
                        newState == PeerConnection.PeerConnectionState.CLOSED
                    ) {
                        viewModelScope.launch {
                            callDisconnected.emit(true)
                        }
                    }
                }
            },
            sendMessageToSocket = { message ->
                socketClient.sendDataToHost(message)
            })
    }

    fun init(serverAddress: String, onError: () -> Unit) {
        startSocketClient(serverAddress, onError)
    }


    private fun startSocketClient(serverAddress: String, onError: () -> Unit) {
        socketClient.init(serverAddress, this@ClientViewModel) {
            onError.invoke()
        }
    }

    override fun onSocketClientOpened() {
        rtcClient.call()
    }

    override fun onSocketClientMessage(messageModel: MessageModel) {
        when(messageModel.type){
            MessageModelType.OFFER -> TODO()
            MessageModelType.ANSWER -> handleAnswer(messageModel)
            MessageModelType.ICE -> handleIceCandidate(messageModel)
        }
    }

    private fun handleIceCandidate(messageModel: MessageModel) {
        runCatching {
            gson.fromJson(messageModel.data.toString(), IceCandidate::class.java)
        }.onSuccess {
            rtcClient.addIceCandidate(it)
        }
    }

    private fun handleAnswer(messageModel: MessageModel) {
        rtcClient.onRemoteSessionReceived(SessionDescription(SessionDescription.Type.ANSWER, messageModel.data.toString()))
    }

    fun startLocalStream(view: SurfaceViewRenderer) {
        rtcClient.startLocalVideo(view)
    }

    fun prepareRemoteSurfaceView(view: org.webrtc.SurfaceViewRenderer) {
        rtcClient.initializeSurfaceView(view)
        remoteView = view
    }

    override fun onCleared() {
        super.onCleared()
        socketClient.onDestroy()
        remoteView?.release()
        remoteView = null
        rtcClient.onDestroy()
    }

}