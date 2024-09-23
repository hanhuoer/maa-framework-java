package io.github.hanhuoer.maa.core.util;

import io.github.hanhuoer.maa.consts.MaaStatusEnum;
import io.github.hanhuoer.maa.ptr.MaaId;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author H
 */
public class TaskFuture<T> extends Future {

    private final Function<MaaId, T> getFunc;

    public TaskFuture(MaaId maaid,
                      Function<MaaId, MaaStatusEnum> statusFunc,
                      Consumer<MaaId> waitFunc,
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
        /*
         * Get the detail of the task.
         *
         * @return The detail of the task.
         */
        return getFunc.apply(getMaaid());
    }
}
