package br.usp.ime.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
 
@RunWith(Suite.class)
@Suite.SuiteClasses({
  br.usp.ime.test.unit.AcquirerWSTest.class,
  br.usp.ime.test.unit.AirlineWSTest.class,
  br.usp.ime.test.unit.TravelAgencyWSTest.class 
})

public class UnitTestSuite {
}