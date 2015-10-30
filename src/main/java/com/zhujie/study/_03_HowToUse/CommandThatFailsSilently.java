package com.zhujie.study._03_HowToUse;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by zhujie on 15/10/28.
 */
public class CommandThatFailsSilently extends HystrixCommand<String> {

    private final boolean throwException;

    public CommandThatFailsSilently(boolean throwException) {
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

    @Override
    protected String getFallback() {
        return null;
    }

    public static class UnitTest {
        @Test
        public void testSuccess() {
            assertEquals(null, new CommandThatFailsSilently(false).execute());
        }

        @Test
        public void testFailure() {
            try {
                assertEquals(null, new CommandThatFailsSilently(true).execute());
            } catch (HystrixRuntimeException e) {
                fail("we should not get an exception as we fail silently with a fallback");
            }
        }
    }
}
