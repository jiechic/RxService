# RxService

[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Jcenter](https://img.shields.io/badge/%20Jcenter%20-1.0.0-5bc0de.svg)](https://bintray.com/jiechic/android/rxservice/_latestVersion)

[^_^]:[![Methods](https://img.shields.io/badge/%20Methods%20%7C%20Size%20-%20239%20%7C%2040%20KB-d9534f.svg)](http://www.methodscount.com/?lib=com.jiechic.android.library%3Arxservice%3A1.0.0)
[^_^]:[![Maven](https://img.shields.io/badge/%20Maven%20-1.0.0-5bc0de.svg)](https://mvnrepository.com/artifact/com.akaita.java/rxservice/1.2.0)
[^_^]:[![Arsenal](https://img.shields.io/badge/%20Arsenal%20-%20RxService%20-4cae4c.svg?style=flat)](https://android-arsenal.com/details/1/6027)


A android library for use service binding with rxjava

# Installation

Using *JCenter*:
```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'com.jiechic.android.library:rxservice:1.0.0'
}
```

# Requirements

RxJava 2.1.0+

sourceCompatibility JavaVersion.VERSION_1_8

targetCompatibility JavaVersion.VERSION_1_8

# Usage

Just enable RxService as soon as possible. In Android, for example:

```kotlin

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxService(this, ServiceHandler::class.java)
                .connectService()
                .map { IServiceHandler.Stub.asInterface(it) }
                .subscribe { Log.d(MainActivity::class.java.simpleName, it.sendAndGet("hello world")) }
    }
}
```