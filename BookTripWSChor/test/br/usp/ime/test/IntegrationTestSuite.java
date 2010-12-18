package br.usp.ime.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
 
@RunWith(Suite.class)
@Suite.SuiteClasses({
  br.usp.ime.test.integration.TravelAgencyIntegrationTest.class,
  br.usp.ime.test.integration.AirlinentegrationTest.class,
  br.usp.ime.test.integration.AcquireIntegrationTest.class
})

public class IntegrationTestSuite {
}