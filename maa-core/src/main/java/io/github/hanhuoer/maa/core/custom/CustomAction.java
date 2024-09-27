package io.github.hanhuoer.maa.core.custom;

import io.github.hanhuoer.maa.callbak.MaaCustomActionCallback;
import io.github.hanhuoer.maa.define.*;
import io.github.hanhuoer.maa.define.base.MaaBool;
import io.github.hanhuoer.maa.model.RecognitionDetail;
import io.github.hanhuoer.maa.model.Rect;
import io.github.hanhuoer.maa.model.TaskDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author H
 */
@Getter
@Accessors(chain = true)
public abstract class CustomAction implements MaaCustomActionCallback {

    private static final Boolean DEFAULT_TASK_SKIP_ENABLED = true;
    private static final Boolean DEFAULT_RECOGNITION_SKIP_ENABLED = true;
    private final Boolean taskDetailSkipEnabled;
    private final Boolean recognitionDetailSkipEnabled;


    public CustomAction() {
        this(DEFAULT_TASK_SKIP_ENABLED, DEFAULT_RECOGNITION_SKIP_ENABLED);
    }

    public CustomAction(Boolean taskDetailSkipEnabled, Boolean recognitionDetailSkipEnabled) {
        if (taskDetailSkipEnabled == null) taskDetailSkipEnabled = DEFAULT_TASK_SKIP_ENABLED;
        if (recognitionDetailSkipEnabled == null) recognitionDetailSkipEnabled = DEFAULT_RECOGNITION_SKIP_ENABLED;
        this.taskDetailSkipEnabled = taskDetailSkipEnabled;
        this.recognitionDetailSkipEnabled = recognitionDetailSkipEnabled;
    }

    public abstract RunResult run(Context context, RunArg arg);


    @Override
    public MaaBool run(
            MaaContextHandle contextHandle,
            MaaTaskId taskId,
            String currentTaskName,
            String customActionName,
            String customActionParam,
            MaaRecoId recoId,
            MaaRectHandle boxHandle,
            MaaCallbackTransparentArg arg
    ) {
        Context context = new Context(contextHandle);

        TaskDetail taskDetail = context.getTasker().getTaskDetail(taskId);
        if (taskDetailSkipEnabled) {
            if (taskDetail == null) return MaaBool.FALSE;
        }

        RecognitionDetail recognitionDetail = context.getTasker().getRecognitionDetail(recoId);
        if (recognitionDetailSkipEnabled) {
            if (recognitionDetail == null) return MaaBool.FALSE;
        }

        RectBuffer rectBuffer = new RectBuffer(boxHandle);
        Rect box = rectBuffer.getValue();
        rectBuffer.close();

        RunArg runArg = new RunArg()
                .setTaskDetail(taskDetail)
                .setCurrentTaskName(currentTaskName)
                .setCustomActionName(customActionName)
                .setCustomActionParam(customActionParam)
                .setRecognitionDetail(recognitionDetail)
                .setBox(box);

        return MaaBool.valueOf(this.run(context, runArg).ifSuccess());
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class RunArg {

        private TaskDetail taskDetail;
        private String currentTaskName;
        private String customActionName;
        private String customActionParam;
        private RecognitionDetail recognitionDetail;
        private Rect box;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class RunResult {

        private Boolean success;

        public boolean ifSuccess() {
            return Boolean.TRUE.equals(success);
        }

        public static RunResult success() {
            return new RunResult().setSuccess(true);
        }

        public static RunResult failed() {
            return new RunResult().setSuccess(false);
        }

    }


}
