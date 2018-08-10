package com.wellsen.modularization.jobschedulerdemo

import android.app.job.JobInfo
import android.app.job.JobInfo.Builder
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    var jobScheduler: JobScheduler? = null
    var jobInfo: JobInfo? = null

    var handler: InComingMessageHandler? = null

    var messenger: Messenger? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler?
        jobInfo = Builder(1, ComponentName(this, MyJobService::class.java))
                .setOverrideDeadline(1 * 1000) //deadline
                .setMinimumLatency(5*1000) //延迟执行
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) //wifi 下执行
                .build()
        jobScheduler?.schedule(jobInfo)
    }

    override fun onStart() {
        super.onStart()
        handler = InComingMessageHandler()
        messenger = Messenger(handler)
        val intent = Intent(this, MyJobService::class.java)
        intent.putExtra("messenger", messenger)
    }

    inner class InComingMessageHandler : Handler() {


        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)


        }
    }
}
