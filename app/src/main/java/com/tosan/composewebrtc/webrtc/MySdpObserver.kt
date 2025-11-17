package com.tosan.composewebrtc.webrtc

import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

open class MySdpObserver : SdpObserver{
    override fun onCreateSuccess(sdp: SessionDescription?) {
        
    }

    override fun onSetSuccess() {
        
    }

    override fun onCreateFailure(error: String?) {
        
    }

    override fun onSetFailure(error: String?) {
        
    }
}