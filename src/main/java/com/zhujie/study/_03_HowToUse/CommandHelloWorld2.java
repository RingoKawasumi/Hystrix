package com.zhujie.study._03_HowToUse;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by zhujie on 15/10/26.
 */
public class CommandHelloWorld2 extends HystrixObservableCommand {

    private final String name;

    public CommandHelloWorld2(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected Observable construct() {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        observer.onNext("Hello");
                        observer.onNext(name + "!");
                        observer.onCompleted();
                    }
                 } catch (Exception e) {
                    observer.onError(e);
                }
            }
        });
    }
}
