package io.github.hanhuoer.maa.core.util;

import io.github.hanhuoer.maa.consts.MaaStatusEnum;
import io.github.hanhuoer.maa.model.TaskDetail;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author H
 */
public class TaskFuture extends Future {

    private final BiFunction<Long, String, Boolean> setParamFunc;
    private final Function<Long, TaskDetail> queryDetailFunc;

    public TaskFuture(Long maaid,
                      Function<Long, MaaStatusEnum> statusFunc,
                      BiFunction<Long, String, Boolean> setParamFunc,
                      Function<Long, TaskDetail> queryDetailFunc) {
        super(maaid, statusFunc);
        this.setParamFunc = setParamFunc;
        this.queryDetailFunc = queryDetailFunc;
    }

    public boolean setParam(String param) {
        /*
         * Set the param of the task.
         *
         * @param param The param of the task.
         * @return True if the param was successfully set, False otherwise.
         */
        return setParamFunc.apply(getMaaid(), param);
    }

    public TaskDetail get() {
        /*
         * Get the detail of the task.
         *
         * @return The detail of the task.
         */
        return queryDetailFunc.apply(getMaaid());
    }
}
