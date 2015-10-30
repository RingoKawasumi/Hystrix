package com.zhujie.study._03_HowToUse;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by zhujie on 15/10/28.
 */
public class CommandThatFailsFast extends HystrixCommand<String> {

    private final boolean throwException;

    public CommandThatFailsFast(boolean throwException) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.throwException = throwException;
    }

    @Override
    protected String run() throws Exception {
        if (throwException) {
            throw new RuntimeException("failure from CommandThatFailsFast");
        } else {
            return "success";
        }
    }

    public static class UnitTest {
        @Test
        public void testSuccess() {
            assertEquals("success", new CommandThatFailsFast(false).execute());
        }

        @Test
        public void testFailure() {
            try {
                new CommandThatFailsFast(true).execute();
                fail("we should have thrown an exception");
            } catch (HystrixRuntimeException e) {
                assertEquals("failure from CommandThatFailsFast", e.getCause().getMessage());
                e.printStackTrace();
            }
        }
    }
}
