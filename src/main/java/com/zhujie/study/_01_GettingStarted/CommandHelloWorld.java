package com.zhujie.study._01_GettingStarted;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import rx.Observable;

import java.util.concurrent.Future;

/**
 * Created by zhujie on 15/10/23.
 */
public class CommandHelloWorld extends HystrixCommand<String> {

    private final String name;

    public CommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        return "Hello " + name + "!";
    }

//    public static void main(String[] args) {
//        String s = new CommandHelloWorld("Bob").execute();
//        Future<String> s1 = new CommandHelloWorld("Bob").queue();
//        Observable<String> s2 = new CommandHelloWorld("Bob").observe();
//    }
}
