package com.wellsen.modularization.jobschedulerdemo

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.os.Messenger
import android.widget.Toast

/**
create by wellsen at 2018/8/10  11:24

 */
class MyJobService : JobService() {

    var messenger: Messenger? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        messenger = intent?.getParcelableExtra("messenger")

        return Service.START_STICKY
    }


    override fun onStartJob(p0: JobParameters?): Boolean {
        handler.sendMessage(Message.obtain(handler, 1, p0))

        return true
    }


    override fun onStopJob(p0: JobParameters?): Boolean {
        handler.removeMessages(1)
        return true
    }

    private var handler = Handler {
        Toast.makeText(applicationContext, "JobService task running", Toast.LENGTH_SHORT).show()

        jobFinished(it.obj as JobParameters?, false)

        return@Handler true
    }


}