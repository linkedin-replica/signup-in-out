package com.linkedin.replica.signing.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArangoSqlSigningHandlerTest.class,
        SigningServiceTest.class
})
public class CoreTestSuite {}
