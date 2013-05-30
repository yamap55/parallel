package com.yamap55.sample.parallel;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.Statement;

public class ParallelSuite2 extends Suite implements Work {

    private ExecutorService _es = new ThreadPoolExecutor(0, Runtime.getRuntime().availableProcessors() * 5, 60L,
            TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

    public ParallelSuite2(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(new ParallelBuilder(), klass, getAnnotatedClasses(klass));
    }

    private static Class<?>[] getAnnotatedClasses(Class<?> klass) throws InitializationError {
        SuiteClasses annotation = klass.getAnnotation(SuiteClasses.class);
        if (annotation == null) {
            throw new InitializationError(String.format("class '%s' must have a SuiteClasses annotation",
                    klass.getName()));
        }
        return annotation.value();
    }

    @Override
    protected Statement childrenInvoker(final RunNotifier notifier) {
        return new Statement() {
            @Override
            public void evaluate() {
                new CompletionServiceUtil(getChildren(), _es).execute(notifier, ParallelSuite2.this);
            }
        };
    }

    @Override
    public void execute(Object obj, RunNotifier notifier) {
        runChild(( Runner ) obj, notifier);
    }
}

class ParallelBuilder extends RunnerBuilder {

    @Override
    public Runner runnerForClass(Class<?> testClass) throws Throwable {
        return new ParallelJUnit4ClassRunner(testClass);
    }
}

class ParallelJUnit4ClassRunner extends BlockJUnit4ClassRunner implements Work {
    private ExecutorService _es = new ThreadPoolExecutor(0, Runtime.getRuntime().availableProcessors() * 5, 60L,
            TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

    ParallelJUnit4ClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement childrenInvoker(final RunNotifier notifier) {
        return new Statement() {
            @Override
            public void evaluate() {
                //                new CompletionServiceUtil(getFilteredChildren(), _es).execute(notifier, ParallelJUnit4ClassRunner.this);
            }
        };
    }

    @Override
    public void execute(Object obj, RunNotifier notifier) {
        runChild(( FrameworkMethod ) obj, notifier);
    }
}

interface Work {
    void execute(Object obj, RunNotifier notifier);
}

class CompletionServiceUtil {
    private final List<Object> _list;

    private final ExecutorService _es;

    CompletionServiceUtil(List<?> list, ExecutorService es) {
        _list = ( List<Object> ) list;
        _es = es;
    }

    void execute(final RunNotifier notifier, final Work run) {
        CompletionService<Object> completionService = new ExecutorCompletionService<Object>(_es);
        for (final Object each : _list) {
            completionService.submit(new Callable<Object>() {
                @Override
                public Object call() {
                    run.execute(each, notifier);
                    return null;
                }
            });
        }
        int n = _list.size();
        for (int i = 0; i < n; i++) {
            try {
                completionService.take().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
