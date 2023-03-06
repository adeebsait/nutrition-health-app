package com.example.finalyear.ui.activityuser.presentation;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavDeepLinkBuilder;

import com.example.finalyear.R;
import com.example.finalyear.UserData;
import com.example.finalyear.common.utils.DietSingleObserve;
import com.example.finalyear.common.utils.SingleLiveEvent;
import com.example.finalyear.common.utils.TimerTicker;
import com.example.finalyear.ui.activityuser.domain.ProtoDbStepFragmentUseCase;
import com.example.finalyear.ui.activityuser.domain.ProtoDbStepServiceUseCase;
import com.example.finalyear.ui.activityuser.domain.UpdateCaloriesUseCase;
import com.example.finalyear.ui.home.domain.GetRemainCaloriesUseCase;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@AndroidEntryPoint
public class StepCounterService extends LifecycleService implements SensorEventListener, TimerTicker.TickListener {

    public static final String BTN_START_STOP = "btn_start";
    public static final String ACTION_KEY = "action_key";
    private Context context;
    private static final int NOTIFICATION_ID = 3036;
    private NotificationManager notificationManager;
    private SensorManager sensorManager;
    private boolean isSensorRegistered = false;
    private Sensor stepSensor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Inject
    public ProtoDbStepServiceUseCase protoDbStepServiceUseCase;
    @Inject
    public GetRemainCaloriesUseCase getRemainCaloriesUseCase;
    @Inject
    public UpdateCaloriesUseCase updateCaloriesUseCase;
    @Inject
    public ProtoDbStepFragmentUseCase protoDbStepFragmentUseCase;
    private TimerTicker timerTicker;
    private final MutableLiveData<Boolean> serviceRunningState = new MutableLiveData<>(false);
    public LiveData<Boolean> serviceRunning = serviceRunningState;

    public SingleLiveEvent<String> completeEvent = new SingleLiveEvent<>();

    private boolean isServiceRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
//TODO: Comment Out For Real Project
//        if (stepSensor == null) {
//            // Toast Shown On OnStart Method
//            return;
//        }

        timerTicker = new TimerTicker(this);

        protoDbStepFragmentUseCase
                .getIsStepAndGoalsEqual()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Boolean aBoolean) {
                        if (aBoolean) {
                            onComplete();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Timber.d(e);
                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete() {
                        stopStepAndService();
                        completeEvent.postValue("Steps Completed");
                    }
                });

        protoDbStepServiceUseCase
                .getIncrementSteps()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Integer steps) {
                        if (notificationManager != null) {
                            notificationManager.notify(NOTIFICATION_ID, Notification()
                                    .setSilent(true)
                                    .setContentText(steps + " STEPS").build());
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        protoDbStepServiceUseCase.insertStepDate(System.currentTimeMillis())
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<UserData>() {
                    @Override
                    public void onComplete(UserData userData) {
                    }
                });

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//TODO: Comment Out For Real Project
//        if (stepSensor == null) {
//            completeEvent.postValue("No step sensor found");
//            return START_STICKY;
//        }
        String intentExtra = intent.getStringExtra(ACTION_KEY);
        if (intentExtra == null) return START_NOT_STICKY;
        if (isServiceRunning) {
            Timber.d("ServiceRunning");
            stopStepAndService();
            return START_NOT_STICKY;
        }
        Timber.d("Service Not Running");
        startStepAndService();
        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopStepAndService();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        compositeDisposable.dispose();
        stopSelf();
    }

    private NotificationCompat.Builder notificationBuilder = null;


    private NotificationCompat.Builder Notification() {
        if (notificationBuilder != null) {
            return notificationBuilder;
        }
        synchronized (this) {

            PendingIntent pendingIntent = new NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.summaryFragment)
                    .createPendingIntent();

            String channelId = getString(R.string.default_notification_channel_id);

            notificationBuilder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getString(R.string.summaray_notification_title))
                    .setContentText("0 STEPS")
                    .setAutoCancel(false)
                    .setContentIntent(pendingIntent);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, getString(R.string.default_notification_channel_id), NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
        }
        return notificationBuilder;
    }

    @SuppressLint("RestrictedApi")
    public void startStepAndService() {// if registered then return no need of again register
        isServiceRunning = true;
        serviceRunningState.postValue(true);
        timerTicker.start();
        startForeground(NOTIFICATION_ID, Notification().setOngoing(true).build());
        if (!isSensorRegistered) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
            isSensorRegistered = true;
        }
        Timber.d("Step Counter: Starting");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void stopStepAndService() {// If not register then no need to unregister just return
        isServiceRunning = false;
        serviceRunningState.postValue(false);
        stopForeground(STOP_FOREGROUND_DETACH);
        Notification().setOngoing(false).setAutoCancel(false).build();
        timerTicker.stop();
        if (isSensorRegistered) {
            sensorManager.unregisterListener(this, stepSensor);
            isSensorRegistered = false;
        }
        Timber.d("Step Counter: Stopping");
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event == null) return;
        protoDbStepServiceUseCase.incrementSteps()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        new DietSingleObserve<UserData>() {
                            @Override
                            public void onComplete(UserData userDataSingle) {
                                Timber.d("Counter Step Increment Success");
                            }
                        }
                );
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private final IBinder binder = new LocalBinder();

    @Override
    public void onTick(long tick) {
        protoDbStepServiceUseCase.addStepDuration()
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<UserData>() {
                    @Override
                    public void onComplete(UserData userData) {
                    }
                });
        protoDbStepServiceUseCase.incrementSteps()
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<UserData>() {
                    @Override
                    public void onComplete(UserData userData) {

                    }
                });
    }

    public class LocalBinder extends Binder {
        StepCounterService getService() {
            return StepCounterService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(@NonNull Intent intent) {
        super.onBind(intent);
        return binder;
    }

}
