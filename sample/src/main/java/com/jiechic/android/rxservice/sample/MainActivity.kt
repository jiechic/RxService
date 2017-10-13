package com.jiechic.android.rxservice.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jiechic.android.rxservice.IServiceHandler
import com.jiechic.android.rxservice.RxService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxService(this, ServiceHandler::class.java)
                .connectService()
                .map { IServiceHandler.Stub.asInterface(it) }
                .subscribe { Log.d(MainActivity::class.java.simpleName, it.sendAndGet("hello world")) }
    }
}
