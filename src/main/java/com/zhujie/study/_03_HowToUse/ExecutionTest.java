package com.zhujie.study._03_HowToUse;

import com.zhujie.study._01_GettingStarted.CommandHelloWorld;
import org.junit.Assert;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by zhujie on 15/10/26.
 */
public class ExecutionTest {

    @Test
    public void testSynchronous() {
        Assert.assertEquals("Hello World!", new CommandHelloWorld("World").execute());
        Assert.assertEquals("Hello Bob!", new CommandHelloWorld("Bob").execute());
    }

    @Test
    public void testAsynchronous1() throws Exception {
        Assert.assertEquals("Hello World!", new CommandHelloWorld("World").queue().get());
        Assert.assertEquals("Hello Bob!", new CommandHelloWorld("Bob").queue().get());
    }

    @Test
    public void testAsynchronous2() throws Exception {
        Future<String> fWorld = new CommandHelloWorld("World").queue();
        Future<String> fBob = new CommandHelloWorld("Bob").queue();

        Assert.assertEquals("Hello World!", fWorld.get());
        Assert.assertEquals("Hello Bob!", fBob.get());
    }

    @Test
    public void testObservable() throws Exception {
        Observable<String> fWorld = new CommandHelloWorld("World").observe();
        Observable<String> fBob = new CommandHelloWorld("Bob").observe();

        //blocking
        Assert.assertEquals("Hello World!", fWorld.toBlocking().single());
        Assert.assertEquals("Hello Bob!", fBob.toBlocking().single());

        //non-blocking
        fWorld.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext: " + s);
            }
        });
        fBob.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("onNext: " + s);
            }
        });
    }
}
