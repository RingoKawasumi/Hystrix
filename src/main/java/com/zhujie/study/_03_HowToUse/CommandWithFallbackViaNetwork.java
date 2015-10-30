package com.zhujie.study._03_HowToUse;

import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by zhujie on 15/10/29.
 */
public class CommandWithFallbackViaNetwork extends HystrixCommand<String> {

    private final int id;

    public CommandWithFallbackViaNetwork(int id) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceX"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("GetValueCommand")));
        this.id = id;
    }

    @Override
    protected String run() throws Exception {
        //        RemoteServiceXClient.getValue(id);
        throw new RuntimeException("force failure for example");
    }

    @Override
    protected String getFallback() {
        return new FallbackViaNetwork(id).execute();
    }

    private static class FallbackViaNetwork extends HystrixCommand<String> {
        private final int id;

        public FallbackViaNetwork(int id) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceX"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("GetValueFallbackCommand"))
                            // use a different threadpool for the fallback command
                            // so saturating the RemoteServiceX pool won't prevent
                            // fallbacks from executing
                    .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("RemoteServiceXFallback")));
            this.id = id;
        }


        @Override
        protected String run() throws Exception {
            //            MemCacheClient.getValue(id);
            throw new RuntimeException("the fallback also failed");
        }

        @Override
        protected String getFallback() {
            // the fallback also failed
            // so this fallback-of-a-fallback will
            // fail silently and return null
            return null;
        }
    }

    public static class UnitTest {
        @Test
        public void test() {
            HystrixRequestContext context = HystrixRequestContext.initializeContext();
            try {
                assertEquals(null, new CommandWithFallbackViaNetwork(1).execute());

                HystrixInvokableInfo<?> command1 = HystrixRequestLog.getCurrentRequest().getAllExecutedCommands().toArray(new HystrixInvokableInfo<?>[2])[0];
                assertEquals("GetValueCommand", command1.getCommandKey().name());
                assertTrue(command1.getExecutionEvents().contains(HystrixEventType.FAILURE));

                HystrixInvokableInfo<?> command2 = HystrixRequestLog.getCurrentRequest().getAllExecutedCommands().toArray(new HystrixInvokableInfo<?>[2])[1];
                assertEquals("GetValueFallbackCommand", command2.getCommandKey().name());
                assertTrue(command2.getExecutionEvents().contains(HystrixEventType.FAILURE));
            } finally {
                context.shutdown();
            }
        }
    }
}
