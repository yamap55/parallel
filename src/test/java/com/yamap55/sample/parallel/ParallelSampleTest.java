package com.yamap55.sample.parallel;

import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ParallelSampleTest {

    private static long starttime = 0L;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Date d = new Date();
        starttime = d.getTime();
        System.out.println("start : " + d);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        Date d = new Date();
        long l = d.getTime() - starttime;
        System.out.println("end   : " + d + ", time : " + l);
    }

    @Test
    public void test1()throws Exception {
        Thread.sleep(5000);
    }

    @Test
    public void test2()throws Exception {
        Thread.sleep(5000);
    }

    //    @Test
    //    public void test3()throws Exception {
    //        Thread.sleep(5000);
    //    }
    //
    //    @Test
    //    public void test4()throws Exception {
    //        Thread.sleep(5000);
    //    }
    //
    //    @Test
    //    public void test5()throws Exception {
    //        Thread.sleep(5000);
    //    }
}
