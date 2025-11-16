package com.tosan.composewebrtc.remote.socket.server

import com.google.gson.Gson
import com.tosan.composewebrtc.utils.MessageModel
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.lang.Exception
import java.net.InetSocketAddress
import javax.inject.Inject

class SocketServer @Inject constructor(
    private val gson: Gson
) {

    private var socketServer: WebSocketServer? = null
    private var memberToCall: WebSocket? = null
    private var socketServerPort = 3013

    fun init(
        listener: SocketServerListener
    ){
        if (socketServer == null){
            socketServer = object : WebSocketServer(InetSocketAddress(socketServerPort)){
                override fun onOpen(
                    conn: WebSocket?,
                    handshake: ClientHandshake?
                ) {
                    if (memberToCall == null) {
                        memberToCall = conn
                    }
                }

                override fun onClose(
                    conn: WebSocket?,
                    code: Int,
                    reason: String?,
                    remote: Boolean
                ) {
                    if (conn == memberToCall) {
                        memberToCall = null
                        listener.onClientDisconnected()
                    }
                }

                override fun onMessage(
                    conn: WebSocket?,
                    message: String?
                ) {
                    runCatching {
                        gson.fromJson(message, MessageModel::class.java)
                    }.onSuccess { data ->
                        listener.onSocketServerNewMessage(data)
                    }
                }

                override fun onError(
                    conn: WebSocket?,
                    ex: Exception?
                ) {
                    if (ex?.message == "Address already in use") {
                        socketServerPort++
                        onDestroy()
                        init(listener)
                    }
                    ex?.printStackTrace()
                }

                override fun onStart() {
                    listener.onStartServer(socketServerPort)
                }

            }.apply { start() }
        }
    }

    fun sendDataToClient(dataModel: MessageModel) {
        runCatching {
            memberToCall?.let {
                val jsonModel = gson.toJson(dataModel)
                memberToCall?.send(
                    jsonModel
                )
            }
        }
    }

    fun onDestroy() = runCatching {
        socketServer?.stop()
        socketServer = null
    }

}