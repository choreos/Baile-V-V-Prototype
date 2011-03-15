package br.usp.ime.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
 
@RunWith(Suite.class)
@Suite.SuiteClasses({
  br.usp.ime.test.acceptance.BookPlanTripTest.class,
  br.usp.ime.test.acceptance.BookPlanTripFlowTest.class,
  br.usp.ime.test.acceptance.OrderTripFlowTest.class,
  br.usp.ime.test.acceptance.ReserveEticketFlowTest.class,
  br.usp.ime.test.acceptance.BookEticketFlowTest.class
})

public class AcceptanceTestSuite {
}