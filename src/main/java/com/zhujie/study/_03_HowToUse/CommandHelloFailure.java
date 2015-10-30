package com.zhujie.study._03_HowToUse;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by zhujie on 15/10/26.
 */
public class CommandHelloFailure extends HystrixCommand<String> {

    private final String name;

    private static final Setter cachedSetter = Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
            .andCommandKey(HystrixCommandKey.Factory.asKey("HelloWorld"))
            .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("HelloWorldPool"));

    public CommandHelloFailure(String name) {
        super(cachedSetter);
//        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        throw new RuntimeException("this command always fails");
    }

    @Override
    protected String getFallback() {
        return "Hello Failure " + name + "!";
    }

    public static class UnitTest {
        @Test
        public void testSynchronous() {
            Assert.assertEquals("Hello Failure World!", new CommandHelloFailure("World").execute());
            Assert.assertEquals("Hello Failure Bob!", new CommandHelloFailure("Bob").execute());
        }
    }
}
