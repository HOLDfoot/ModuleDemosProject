package com.holdfoot.secretlisa.rxjavademo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.holdfoot.secretlisa.rxjavademo.utils.RxUtil;
import com.holdfoot.secretlisa.rxjavademo.utils.RxUtil.NextCallback;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String Tag = "zmr";
    private TextView textView;
    private Button button;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textview);
        button = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.imageview);

        //simpleFlowable();
        //flowableSubscribeConsumer();
        //rexSchedulerMap();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                observableObserver();
            }
        });
    }

    private void testRxJava() {
        // 测试定时器timer
        NextCallback nextCallback = new NextCallback() {

            public void backgroundSubscribe() {
                Log.d(Tag, "backgroundSubscribe");
            }

            public void subscribe() {
                Log.d(Tag, "subscribe");
            }

            public boolean next() {
                Log.d(Tag, "next");
                return false;
            }

            public void complete() {
                Log.d(Tag, "complete");
            }
        };
        RxUtil.timer(1000, 30, 2000, nextCallback);
        // 通过next的返回值的true和false控制timer是否继续执行
        rxUtilTimer();
        // 测试延迟执行
        rxUtilDelay();
        // 测试切换线程执行
        rxUtilDoOnUIThread();
    }

    private void testThisClassMethod() {
        rexSchedulerMap();
        observableObserver();
        initFirstRxAndroid();
        flowable();
        simpleFlowable();
        flowableSubscribeConsumer();
        ObservableJust();
        callCode(0);
    }

    /* 生成一个默认的Subscriber */
    private Subscriber<String> createSubscriber() {
        Subscriber<String> subscriber;
        subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.e(Tag, "createSubscriber onSubscribe s = " + s);
            }

            @Override
            public void onNext(String s) {
                Log.e(Tag, "createSubscriber onNext s = " + s);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(Tag, "createSubscriber onError t = " + t.getLocalizedMessage());
            }

            @Override
            public void onComplete() {
                Log.e(Tag, "createSubscriber onComplete");
            }
        };
        return subscriber;
    }

    int n = 1;
    /* 使用rxUtil的timer发起调度 */
    private void rxUtilTimer() {
        RxUtil.timer(0, 1000, 100, new NextCallback() {
            @Override
            public void backgroundSubscribe() {
                int x = 0;
                while (x < 1000) {
                    Log.d("xxx", "yyyy");
                    x++;
                }
                Log.d(Tag, "testOne backgroundSubscribe " + Thread.currentThread().getId());
            }

            @Override
            public void subscribe() {
                Log.d(Tag, "testOne subscribe " + Thread.currentThread().getId());

            }

            @Override
            public boolean next() {
                n++;
                Log.d(Tag, "testOne next " + Thread.currentThread().getId() + " n = " + n);
                if (n == 10) {
                    return false;
                }
                return true;
            }

            @Override
            public void complete() {
                int y = 0;
                while (y < 1000) {
                    Log.d("xxx", "yyyy " + y);
                    y++;
                }
                Log.d(Tag, "testOne complete " + Thread.currentThread().getId());

            }
        });
    }

    /* 在非UI线程通过Callback操作UI */
    private void rxUtilDoOnUIThread() {
        Log.e(Tag, "runOnUIThread threadId = " + Thread.currentThread().getId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(Tag, "runOnUIThread run threadId = " + Thread.currentThread().getId());
                RxUtil.doOnUIThread(new RxUtil.Code() {
                    @Override
                    public void call() {
                        Log.e(Tag, "runOnUIThread call threadId = " + Thread.currentThread().getId());
                        textView.setText("runOnUIThread call");
                    }
                });

            }
        }).start();
    }

    // 在新起的线程发起调用
    public static void callCode(final int code) {
        Log.e(Tag, "callCode");
        Flowable.create((FlowableOnSubscribe<Boolean>) e -> {
            //code.call();
            Thread.sleep(2000);
            Log.e(Tag, "callCode subscribe");
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread()).subscribe();
        Log.e(Tag, "callCode end");
    }

    /* 延迟执行一个任务 */
    private void rxUtilDelay() {
        Log.e(Tag, new SimpleDateFormat().format(new Date()) + "");
        RxUtil.delay(3000, new RxUtil.Callback() {
            @Override
            public void callcack() {
                Log.e(Tag, new SimpleDateFormat().format(new Date()) + "");
            }
        });
    }

    /* Observable.just */
    private void ObservableJust() {
        Observable.just("hello ObservableJust").subscribe(s -> {
            textView.setText(s + " 111");
            Log.e(Tag, s + " 111");
        });

        Observable.just("hello ObservableJust").map(new Function<String, Object>() {
            @Override
            public Object apply(@NonNull String s) throws Exception {
                Log.e(Tag, s + "apply 222");
                return s + "apply 222";
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object s) throws Exception {
                Log.e(Tag, s + "accept 222");
                textView.setText(s.toString());
            }
        });

        Observable.just("Hello ObservableJust map").map(new Function<String, String>() {
            @Override
            public String apply(@NonNull String s) throws Exception {
                return s + " s map";
            }
        }).subscribe((String s1) -> {
            textView.setText(s1);
        });
    }

    /* Flowable.just */
    private void flowableSubscribeConsumer() {
        Log.i(Tag, "flowableSubscribeConsumer");
        Flowable.just("Hello flowableSubscribeConsumer")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.e(Tag, "consumer s = " + s);
                    }
                });
    }

    /* Flowable.just只发射订阅 */
    private void simpleFlowable() {
        Log.i(Tag, "simpleFlowable");
        Flowable.just("Hello SimpleFlowable")
                .subscribe(createSubscriber()); // 不会执行到subscriber的onNext方法, 但是会执行到onSubscribe方法
    }

    /* Flowable.create 可以做任务发射和控制 */
    private void flowable() {
        Subscriber<String> subscriber = createSubscriber();
        Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                Log.e(Tag, "flowable subscribe e " + e.toString()); // success
                e.onNext("Hello flowable Next");
                e.onNext("Hello flowable Next");
                e.onComplete();
                e.onNext("Hello flowable Next");
            }
        }, BackpressureStrategy.BUFFER);
        flowable.subscribe(subscriber);
    }

    /* 任务调度, 可以通过filter, map, doOnNext等 */
    private void rexSchedulerMap() {
        Flowable<Bitmap> bitmapFlowable = Flowable.just(R.mipmap.ic_launcher)
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        Log.i(Tag, "test integer resId= " + integer);
                        return false;
                    }
                })
                .map(new Function<Integer, Bitmap>() {
                    @Override
                    public Bitmap apply(@NonNull Integer integer) throws Exception {
                        Log.e(Tag, "这是在io线程的做的bitmap绘制圆形");
                        Thread.sleep(1000);
                        return BitmapFactory.decodeResource(getResources(), integer);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@NonNull Bitmap bitmap) throws Exception {
                        Log.e(Tag, "这是在Main线程做的UI操作");
                        imageView.setImageBitmap(bitmap);
                    }
                });
        bitmapFlowable.subscribe();
    }

    /* 通过Observer处理的任务调度 */
    private void observableObserver() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("第一个");
                //e.onNext("第二个");
                //e.onComplete();
            }
        });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e(Tag, "observableObserver onSubscribe isDisposed = " + d.isDisposed());
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.e(Tag, "observableObserver string = " + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(Tag, "observableObserver onError e = " + e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {
                Log.e(Tag, "observableObserver onComplete");
            }
        };
        observable.subscribe(observer);
    }

    /* 通过Consumer处理的任务调度 */
    private void initFirstRxAndroid() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                Thread.sleep(2000);
                e.onNext(2);
                Thread.sleep(3000);
                e.onNext(3);
                Thread.sleep(4000);
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(Tag, "accept:" + integer);
                    }
                });

    }
}
