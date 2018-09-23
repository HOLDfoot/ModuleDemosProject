package com.holdfoot.secretlisa.rxjavademo.utils;

import android.util.Log;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ZhuMingren on 2017/7/18.
 */

public class RxUtil {

    public static final String Tag  = RxUtil.class.getSimpleName();

    public static void timer(int delay, final int counts, int interval, NextCallback nextCallback) {
        Log.e(Tag, "timer threadId = " + Thread.currentThread().getId());
        final Disposable[] mDisposable = new Disposable[1];
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                Log.e(Tag, "subscribe threadId = " + Thread.currentThread().getId());
                if (nextCallback != null) {
                    nextCallback.backgroundSubscribe();
                }

                Thread.sleep(delay);

                for (int i = 0; i < counts; i ++) {
                    e.onNext("");

                    Log.d("zhumr", "isDisposed = " + e.isDisposed() + " mDisposable = " + mDisposable[0].isDisposed());
                    if (e.isDisposed()) {
                        return;
                    }

                    if (i ==counts -1) {
                        e.onComplete();
                    } else {
                        Thread.sleep(interval);
                    }
                }
            }
        });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable[0] = d;
                Log.e(Tag, "onSubscribe isDisposed = " + d.isDisposed() + " threadId = " + Thread.currentThread().getId());
                if (nextCallback != null) {
                    nextCallback.subscribe();
                }
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.e(Tag, "onNext threadId = " + Thread.currentThread().getId());
                if (nextCallback != null) {
                   boolean isContinue =  nextCallback.next();
                    Log.e(Tag, "onNext isContinue = " + isContinue);
                    if (!isContinue) {
                        mDisposable[0].dispose();
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(Tag, "observableObserver onError e = " + e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {
                Log.e(Tag, "observableObserver onComplete");
                if (nextCallback != null) {
                    nextCallback.complete();
                }
            }
        };
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public static void delay(int millis, Callback callback) {
        Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Boolean> e) throws Exception {
                Thread.sleep(millis);
                e.onNext(Boolean.valueOf(true));
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean bool) throws Exception {
                        Log.e(Tag, "bool: " + bool);
                        if (bool) {
                            callback.callcack();
                        }
                    }
                });
    }

    public static void doOnUIThread(Code code) {
        Flowable.create((FlowableOnSubscribe<Boolean>) e -> {
            e.onNext(true);
        }, BackpressureStrategy.BUFFER)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                     code.call();
                    }
                });

    }

    public interface Callback {
        void callcack();
    }

    public interface Code {
        void call();
    }

    public static abstract class NextCallback {
        public boolean shouldCancel = false;
        public void cancel() {
            shouldCancel = true;
        }
        public abstract void backgroundSubscribe();
        public abstract void subscribe();
        public abstract boolean next();
        public abstract void complete();
    }
}
