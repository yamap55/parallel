package com.yamap55.sample.parallel.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.yamap55.sample.parallel.ParallelSample2Test;
import com.yamap55.sample.parallel.ParallelSampleTest;

@RunWith(Suite.class)
@SuiteClasses({ParallelSample2Test.class, ParallelSampleTest.class})
public class ParallelSample3Test {
}
