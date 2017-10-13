package com.jiechic.android.rxservice.sample

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.jiechic.android.rxservice.IServiceHandler

/**
 * Created by jiechic on 2017/10/12.
 */
class ServiceHandler : Service() {
    override fun onBind(intent: Intent?): IBinder {
        return object : IServiceHandler.Stub() {
            override fun sendAndGet(msg: String?): String {
                return msg ?: ""
            }
        }
    }
}