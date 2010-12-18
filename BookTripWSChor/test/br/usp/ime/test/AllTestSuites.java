package br.usp.ime.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
 
@RunWith(Suite.class)
@Suite.SuiteClasses({
  br.usp.ime.test.UnitTestSuite.class,
  br.usp.ime.test.IntegrationTestSuite.class,
  br.usp.ime.test.AcceptanceTestSuite.class 
})

public class AllTestSuites {
}