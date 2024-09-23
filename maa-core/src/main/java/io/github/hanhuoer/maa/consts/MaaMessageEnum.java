package io.github.hanhuoer.maa.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author H
 */
@AllArgsConstructor
@Getter
public enum MaaMessageEnum {

    INVALID("Invalid"),

    RESOURCE_LOADING_STARTING("Resource.Loading.Starting"),
    RESOURCE_LOADING_SUCCEEDED("Resource.Loading.Succeeded"),
    RESOURCE_LOADING_FAILED("Resource.Loading.Failed"),

    CONTROLLER_ACTION_STARTING("Controller.Action.Starting"),
    CONTROLLER_ACTION_SUCCEEDED("Controller.Action.Succeeded"),
    CONTROLLER_ACTION_FAILED("Controller.Action.Failed"),

    TASKER_TASK_STARTING("Tasker.Task.Starting"),
    TASKER_TASK_SUCCEEDED("Tasker.Task.Succeeded"),
    TASKER_TASK_FAILED("Tasker.Task.Failed"),

    TASK_NEXTLIST_STARTING("Task.NextList.Starting"),
    TASK_NEXTLIST_SUCCEEDED("Task.NextList.Succeeded"),
    TASK_NEXTLIST_FAILED("Task.NextList.Failed"),

    TASK_RECOGNITION_STARTING("Task.Recognition.Starting"),
    TASK_RECOGNITION_SUCCEEDED("Task.Recognition.Succeeded"),
    TASK_RECOGNITION_FAILED("Task.Recognition.Failed"),

    TASK_ACTION_STARTING("Task.Action.Starting"),
    TASK_ACTION_SUCCEEDED("Task.Action.Succeeded"),
    TASK_ACTION_FAILED("Task.Action.Failed"),
    ;


    private final String value;

    public static MaaMessageEnum of(String value) {
        for (MaaMessageEnum item : values()) {
            if (item.value.equals(value)) return item;
        }
        return null;
    }

}
