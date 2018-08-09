package com.wellsen.modularization.messengerdemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var messenger: Messenger? = null
    var connection: LocalServiceConnection? = null
    var service: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        service = Intent(this, MyRemoteService::class.java)
        startService(service)
        connection = LocalServiceConnection()
        bindService(service, connection, Context.BIND_AUTO_CREATE)
        findViewById<TextView>(R.id.tv_message_send).setOnClickListener {
            Log.e("wellsen", "准备发送消息")
            val messege = Message.obtain()
            messege.what = 1
            messenger?.send(messege)
        }
    }

    inner class LocalServiceConnection : android.content.ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            messenger = Messenger(service)
        }

        override fun onServiceDisconnected(name: ComponentName) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
        stopService(intent)
    }
}
