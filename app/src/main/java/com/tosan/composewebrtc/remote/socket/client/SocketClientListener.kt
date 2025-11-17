package com.tosan.composewebrtc.remote.socket.client

import com.tosan.composewebrtc.utils.MessageModel

interface SocketClientListener {
    fun onSocketClientOpened()
    fun onSocketClientMessage(messageModel: MessageModel)
}