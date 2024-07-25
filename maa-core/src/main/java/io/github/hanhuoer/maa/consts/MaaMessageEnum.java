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

    RESOURCE_STARTLOADING("Resource.StartLoading"),
    RESOURCE_LOADINGCOMPLETED("Resource.LoadingCompleted"),
    RESOURCE_LOADINGFAILED("Resource.LoadingFailed"),

    CONTROLLER_UUIDGOT("Controller.UUIDGot"),
    CONTROLLER_UUIDGETFAILED("Controller.UUIDGetFailed"),
    CONTROLLER_RESOLUTIONGOT("Controller.ResolutionGot"),
    CONTROLLER_RESOLUTIONGETFAILED("Controller.ResolutionGetFailed"),
    CONTROLLER_SCREENCAPINITED("Controller.ScreencapInited"),
    CONTROLLER_SCREENCAPINITFAILED("Controller.ScreencapInitFailed"),
    CONTROLLER_TOUCHINPUTINITED("Controller.TouchinputInited"),
    CONTROLLER_TOUCHINPUTINITFAILED("Controller.TouchinputInitFailed"),
    CONTROLLER_KEYINPUTINITED("Controller.KeyinputInited"),
    CONTROLLER_KEYINPUTINITFAILED("Controller.KeyinputInitFailed"),
    CONTROLLER_CONNECTSUCCESS("Controller.ConnectSuccess"),
    CONTROLLER_CONNECTFAILED("Controller.ConnectFailed"),
    CONTROLLER_ACTION_STARTED("Controller.Action.Started"),
    CONTROLLER_ACTION_COMPLETED("Controller.Action.Completed"),
    CONTROLLER_ACTION_FAILED("Controller.Action.Failed"),

    TASK_STARTED("Task.Started"),
    TASK_COMPLETED("Task.Completed"),
    TASK_FAILED("Task.Failed"),
    TASK_FOCUS_READYTORUN("Task.Focus.ReadyToRun"),
    TASK_FOCUS_RUNOUT("Task.Focus.Runout"),
    TASK_FOCUS_COMPLETED("Task.Focus.Completed"),
    TASK_DEBUG_READYTORUN("Task.Debug.ReadyToRun"),
    TASK_DEBUG_RUNOUT("Task.Debug.Runout"),
    TASK_DEBUG_COMPLETED("Task.Debug.Completed"),
    TASK_DEBUG_LISTTORECOGNIZE("Task.Debug.ListToRecognize"),
    TASK_DEBUG_RECOGNITIONRESULT("Task.Debug.RecognitionResult"),
    TASK_DEBUG_HIT("Task.Debug.Hit"),
    TASK_DEBUG_MISSALL("Task.Debug.MissAll"),
    ;


    private final String value;

    public static MaaMessageEnum of(String value) {
        for (MaaMessageEnum item : values()) {
            if (item.value.equals(value)) return item;
        }
        return null;
    }

    public static void main(String[] args) {
        MaaMessageEnum maaMessageEnum = MaaMessageEnum.of("Task.Debug.MissAll");
        System.out.println(maaMessageEnum);

    }

}
