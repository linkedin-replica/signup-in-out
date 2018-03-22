package com.linkedin.replica.signing.tests.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArangoSqlSigningHandlerTest.class,
        MysqlSigningHandlerTest.class,
        SigningServiceTest.class
})
public class CoreTestSuite {}
