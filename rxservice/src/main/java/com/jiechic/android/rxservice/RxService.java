package com.jiechic.android.rxservice;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Cancellable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by jiechic on 2017/6/22.
 */

public class RxService {
    private final Context context;
    private final BehaviorSubject<IBinder> serviceSubject = BehaviorSubject.create();
    private final Class<? extends Service> aClass;
    private final Intent serviceIntent;
    private ServiceConnection serviceConnection;


    public RxService(Context context, Class<? extends Service> aClass) {
        this.context = context;
        this.aClass = aClass;
        this.serviceIntent = new Intent(context, aClass);
    }

    private Observable<IBinder> connect() {
        return Observable
                .create(subscriber -> {
                    synchronized (RxService.this) {
                        if (serviceConnection == null) {
                            serviceConnection = new ServiceConnection() {
                                @Override
                                public void onServiceConnected(ComponentName name, IBinder service) {
                                    serviceSubject.onNext(service);
                                }

                                @Override
                                public void onServiceDisconnected(ComponentName name) {
                                    serviceSubject.onComplete();
                                }
                            };
                            boolean bound = context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
                            subscriber.setCancellable(() -> {
                                if (bound) {
                                    context.unbindService(serviceConnection);
                                }
                                serviceConnection = null;
                            });
                        }
                    }
                    subscriber.onNext(serviceConnection);
                })
                .flatMap(connection -> serviceSubject);
    }


    private Observable<Boolean> startService(boolean stayAlive, boolean startAgain) {
        return Observable.defer(() -> Observable.create(subscriber -> {
            if (startAgain) {
                context.startService(serviceIntent);
            } else {
                boolean isRunning = isRunning(context, aClass);
                if (stayAlive && !isRunning) {
                    context.startService(serviceIntent);
                }
            }
            subscriber.onNext(startAgain);
            subscriber.onComplete();
        }));
    }

    private static boolean isRunning(Context context, Class aClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (aClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public Observable<IBinder> connectService() {
        return connectService(false);
    }

    public Observable<IBinder> connectService(boolean stayAlive) {
        return connectService(stayAlive, false);
    }

    public Observable<IBinder> connectService(boolean stayAlive, boolean startAgain) {
        return startService(stayAlive, startAgain)
                .flatMap(started -> connect());
    }
}
