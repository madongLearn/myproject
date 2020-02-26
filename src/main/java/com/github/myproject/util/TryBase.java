package com.github.myproject.util;

import java.util.function.Supplier;

/**
 * Created by madong on 2016/10/28.
 */
public class TryBase<T> {
    @FunctionalInterface
    public static interface Callable<T> {
        T apply() throws Exception;
    }

    @FunctionalInterface
    public static interface Runnable {
        void apply() throws Exception;
    }

    @FunctionalInterface
    public static interface Consumer<P> {
        void accept(P p) throws Exception;
    }

    @FunctionalInterface
    public static interface Function<P, R> {
        R apply(P p) throws Exception;
    }

    public static class TryFailedException extends RuntimeException {
        TryFailedException(Exception e) {
            super(e);
        }

        TryFailedException(String message) {
            super(message);
        }
    }

    public static <T> TryBase<T> of(Callable<T> callable) {
        return of(1, callable);
    }

    public static <T> TryBase<T> of(int times, Callable<T> callable) {
        return of(times, (i) -> callable.apply());
    }

    public static <T> TryBase<T> of(int times, Function<Integer, T> function) {
        if (times <= 0) {
            throw new TryFailedException("times accept positive number");
        }

        TryBase<T> ofc = null;
        for (int i = 0; i < times; i++) {
            try {
                return TryBase.success(function.apply(i));
            } catch (Exception e) {
                ofc = TryBase.failed(e);
            }
        }

        return ofc;
    }

    public static TryBase<Void> run(Runnable runnable) {
        return run(1, runnable);
    }

    public static TryBase<Void> run(int times, Runnable runnable) {
        return run(times, (i) -> runnable.apply());
    }

    public static TryBase<Void> run(int times, Consumer<Integer> consumer) {
        if (times <= 0) {
            throw new TryFailedException("times accept positive number");
        }

        TryBase<Void> ofr = null;
        for (int i = 0; i < times; i++) {
            try {
                consumer.accept(i);
                return TryBase.success(null);
            } catch (Exception e) {
                ofr = TryBase.failed(e);
            }
        }

        return ofr;
    }

    public static void tryRun(Runnable runnable) throws TryFailedException {
        try {
            runnable.apply();
        } catch (Exception e) {
            throw new TryFailedException(e);
        }
    }

    /**
     * @param runnable runnable
     * @return java.lang.Runnable throw TryFailedException while has Exception
     */
    public static java.lang.Runnable toThreadRun(Runnable runnable) {
        return () -> {
            try {
                runnable.apply();
            } catch (Exception e) {
                throw new TryFailedException(e);
            }
        };
    }

    private TryBase() {

    }

    private Exception cause = null;
    private T result = null;

    private static <T> TryBase<T> failed(Exception e) {
        TryBase<T> impl = new TryBase<>();
        impl.cause = e;
        return impl;
    }

    private static <T> TryBase<T> success(T result) {
        TryBase<T> impl = new TryBase<>();
        impl.result = result;
        return impl;
    }

    public boolean isFailure() {
        return !isSuccess();
    }

    public T orElse(T other) {
        return isSuccess() ? get() : other;
    }

    public T orElseGet(Supplier<T> supplier) {
        return isSuccess() ? get() : supplier.get();
    }

    public boolean isSuccess() {
        return null == cause;
    }

    public Exception getCause() {
        return cause;
    }

    public T get() {
        if (isFailure()) {
            throw new TryFailedException(cause);
        }
        return result;
    }
}
