package cn.donica.slcd.sensor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import cn.donica.slcd.sensor.net.Retrofits;
import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "Donica";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Retrofits.getBrand(this);
       //Observable<String> observable = Observable.just("hehe","haha");
        Observable<String> observable=Observable.from(new String[]{"ni","hao","a","forever"});
        Observable<String> observable1=observable.filter(new Func1<String, Boolean>() {
           @Override
           public Boolean call(String s) {
               Log.i(TAG,"call");
               return s.contains("ni")||s.contains("f");
           }
       }).subscribeOn(Schedulers.io());
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG,"onCompleted");
            }

            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG,"onError");
            }

            @Override
            public void onNext(String o) {
                Log.i(TAG,o);
            }
        };

        observable1.subscribe(observer);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
