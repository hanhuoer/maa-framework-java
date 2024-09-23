package io.github.hanhuoer.maa.core.custom;

import io.github.hanhuoer.maa.model.RecognitionDetail;
import io.github.hanhuoer.maa.model.Rect;
import io.github.hanhuoer.maa.model.TaskDetail;
import io.github.hanhuoer.maa.ptr.*;
import io.github.hanhuoer.maa.ptr.base.MaaBool;
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
public abstract class CustomAction {

    private final MaaCustomActionHandle handle;


    public CustomAction() {
        this.handle = new MaaCustomActionHandle();
        handle.action = this::runAgent;
    }

    public abstract RunResult run(Context context, RunArg arg);


    public MaaBool runAgent(
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
        if (taskDetail == null) return MaaBool.FALSE;

        RecognitionDetail recognitionDetail = context.getTasker().getRecognitionDetail(recoId);
        if (recognitionDetail == null) return MaaBool.FALSE;

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

    }


}
