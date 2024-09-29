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

    @SneakyThrows
    public T get() {
        /*
         * Get the detail of the task.
         *
         * @return The detail of the task.
         */
        int times = 0;
        while (times++ < 5) {
            T result = getFunc.apply(getMaaid());
            if (log.isDebugEnabled())
                log.debug("times: {}; result: {}", times, result);
            if (result != null) {
                return result;
            }
            Thread.sleep(10);
        }
        return getFunc.apply(getMaaid());
    }
}
