package io.github.hanhuoer.maa.core.util;

import io.github.hanhuoer.maa.consts.MaaStatusEnum;
import io.github.hanhuoer.maa.define.MaaId;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author H
 */
@Slf4j
public class TaskFuture<T> extends Future {

    private final Function<MaaId, T> getFunc;

    public TaskFuture(MaaId maaid,
                      Function<MaaId, MaaStatusEnum> statusFunc,
                      Function<MaaId, Void> waitFunc,
                      Function<MaaId, T> getFunc) {
        super(maaid, statusFunc, waitFunc);
        this.getFunc = getFunc;
    }

    @Override
    public TaskFuture<T> waiting() {
        super.waiting();
        return this;
    }

    public T get() {
        return get(10, 5);
    }

    public T get(long intervalMillis) {
        return get(intervalMillis, 5);
    }

    /**
     * Get the detail of the task.
     *
     * @param intervalMillis interval.
     * @param times          times.
     * @return The detail of the task.
     */
    @SneakyThrows
    public T get(long intervalMillis, int times) {
        if (intervalMillis <= 0) {
            intervalMillis = 0;
        }
        if (times <= 0) {
            times = 1;
        }

        int i = 0;
        while (i++ < times) {
            T result = getFunc.apply(getMaaid());
            if (log.isDebugEnabled())
                log.debug("times: {}/{}; result: {}", i, times, result);
            if (result != null) {
                return result;
            }
            Thread.sleep(intervalMillis);
        }
        return getFunc.apply(getMaaid());
    }
}
