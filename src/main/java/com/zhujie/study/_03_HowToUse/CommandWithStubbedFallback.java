package com.zhujie.study._03_HowToUse;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by zhujie on 15/10/29.
 */
public class CommandWithStubbedFallback extends HystrixCommand<CommandWithStubbedFallback.UserAccount> {

    private final int customerId;
    private final String countryCodeFromGeoLookup;

    public CommandWithStubbedFallback(int customerId, String countryCodeFromGeoLookup) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.customerId = customerId;
        this.countryCodeFromGeoLookup = countryCodeFromGeoLookup;
    }

    @Override
    protected UserAccount run() throws Exception {
        // fetch UserAccount from remote service
        //        return UserAccountClient.getAccount(customerId);
        throw new RuntimeException("forcing failure for example");
    }

    @Override
    protected UserAccount getFallback() {
        return new UserAccount(customerId, "UnKnow name", countryCodeFromGeoLookup, true, true, false);
    }

    public static class UserAccount {
        private final int customerId;
        private final String name;
        private final String countryCode;
        private final boolean isFeatureXPermitted;
        private final boolean isFeatureYPermitted;
        private final boolean isFeatureZPermitted;

        public UserAccount(int customerId, String name, String countryCode, boolean isFeatureXPermitted,
                           boolean isFeatureYPermitted, boolean isFeatureZPermitted) {
            this.customerId = customerId;
            this.name = name;
            this.countryCode = countryCode;
            this.isFeatureXPermitted = isFeatureXPermitted;
            this.isFeatureYPermitted = isFeatureYPermitted;
            this.isFeatureZPermitted = isFeatureZPermitted;
        }
    }

    public static class UnitTest {
        @Test
        public void test() {
            CommandWithStubbedFallback command = new CommandWithStubbedFallback(1234, "ca");
            UserAccount account = command.execute();
            assertTrue(command.isFailedExecution());
            assertTrue(command.isResponseFromFallback());
            assertEquals(1234, account.customerId);
            assertEquals("ca", account.countryCode);
            assertEquals(true, account.isFeatureXPermitted);
            assertEquals(true, account.isFeatureYPermitted);
            assertEquals(false, account.isFeatureZPermitted);
        }
    }
}
