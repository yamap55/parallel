package com.yamap55.sample.parallel;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.Statement;

public class ParallelSuite extends Suite{

    public ParallelSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(builder, klass, getAnnotatedClasses(klass));
    }

    private static Class<?>[] getAnnotatedClasses(Class<?> klass) throws InitializationError {
        SuiteClasses annotation= klass.getAnnotation(SuiteClasses.class);
        if (annotation == null) {
            throw new InitializationError(String.format("class '%s' must have a SuiteClasses annotation", klass.getName()));
        }
        return annotation.value();
    }

    @Override
    protected Statement childrenInvoker(final RunNotifier notifier) {
        return new Statement() {
            @Override
            public void evaluate() {
                runChildren(notifier);
            }
        };
    }

    private void runChildren(final RunNotifier notifier) {
        ExecutorService es = Executors.newCachedThreadPool();
        CompletionService<Object> completionService =
                new ExecutorCompletionService<Object>(es);
        for (final Runner each : getChildren()) {
            completionService.submit(new Callable<Object>(){
                @Override
                public Object call() {
                    runChild(each, notifier);
                    return null;
                }
            });
        }
        int n = getChildren().size();
        try {
            for (int i = 0; i < n; i++) {
                try {
                    completionService.take().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            es.shutdown();
        }
    }
}
