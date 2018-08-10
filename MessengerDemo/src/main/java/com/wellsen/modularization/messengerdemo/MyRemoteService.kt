package com.wellsen.modularization.messengerdemo

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log

class MyRemoteService : Service() {
    var messenger: Messenger? = null

    override fun onCreate() {
        super.onCreate()
        val handler = LocalHandler()
        Log.e(javaClass.simpleName, "创建::$this")
        messenger = Messenger(handler)
    }

    private class LocalHandler : Handler() {
        override fun handleMessage(msg: Message) {
            Log.e("wellsen", "receive obj: " + msg.obj +"  what::"+ msg.what)
        }
    }

    override fun onBind(intent: Intent): IBinder {
        Log.e("wellsen", "绑定::$messenger")
        return messenger?.binder!!
    }
}
