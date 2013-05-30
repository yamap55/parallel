package com.yamap55.sample.parallel.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.yamap55.sample.parallel.ParallelSample2Test;
import com.yamap55.sample.parallel.ParallelSampleTest;
import com.yamap55.sample.parallel.ParallelSuite;

@RunWith(ParallelSuite.class)
@SuiteClasses({ParallelSample2Test.class, ParallelSampleTest.class})
public class ParallelSample4Test {
}
