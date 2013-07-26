package com.lanthaps.identime;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CommonInterceptorTest.class, SecureTokenServiceTest.class, LocalUserServiceTest.class })
public class AllTests {

}
