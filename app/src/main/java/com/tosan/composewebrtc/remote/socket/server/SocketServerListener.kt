package com.tosan.composewebrtc.remote.socket.server

import com.tosan.composewebrtc.utils.MessageModel

interface SocketServerListener {
    fun onSocketServerNewMessage(message: MessageModel)
    fun onStartServer(port:Int)
    fun onClientDisconnected()
}