package io.github.hanhuoer.maa.core.custom;

import io.github.hanhuoer.maa.model.Rect;
import io.github.hanhuoer.maa.model.TaskDetail;
import io.github.hanhuoer.maa.ptr.StringBuffer;
import io.github.hanhuoer.maa.ptr.*;
import io.github.hanhuoer.maa.ptr.base.MaaBool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author H
 */
@Getter
@Accessors(chain = true)
public abstract class CustomRecognizer {

    private final MaaCustomRecognitionHandle handle;

    public CustomRecognizer() {
        this(new MaaCustomRecognitionHandle());
    }

    private CustomRecognizer(MaaCustomRecognitionHandle handle) {
        this.handle = handle;
        handle.recognizer = this::analyzeAgent;
    }

    /**
     * analyze the given image.
     *
     * @param context the context.
     * @param arg     arg.
     * @return analyze result object.
     */
    public abstract AnalyzeResult analyze(Context context, AnalyzeArg arg);


    public MaaBool analyzeAgent(
            MaaContextHandle contextHandle,
            MaaTaskId taskId,
            String currentTaskName,
            String customRecoName,
            String customRecoParam,
            MaaImageBufferHandle imageHandle,
            MaaRectHandle roi,
            MaaCallbackTransparentArg transparentArg,
            MaaRectHandle outBox,
            MaaStringBufferHandle outDetail
    ) {
        Context context = new Context(contextHandle);

        TaskDetail taskDetail = context.tasker().getTaskDetail(taskId);
        if (taskDetail == null) return MaaBool.FALSE;

        ImageBuffer imageBuffer = new ImageBuffer(imageHandle);
        BufferedImage image = null;
        try {
            image = imageBuffer.getValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        RectBuffer roiBuffer = new RectBuffer(roi);
        AnalyzeArg analyzeArg = new AnalyzeArg()
                .setTaskDetail(taskDetail)
                .setCurrentTaskName(currentTaskName)
                .setCustomRecognitionName(customRecoName)
                .setCustomRecognitionParam(customRecoParam)
                .setImage(image)
                .setRoi(roiBuffer.getValue());

        AnalyzeResult result = this.analyze(context, analyzeArg);

        if (result.getBox() != null) {
            RectBuffer rectBuffer = new RectBuffer(outBox);
            rectBuffer.setValue(result.getBox());
        }

        StringBuffer stringBuffer = new StringBuffer(outDetail);
        stringBuffer.setValue(result.getDetail());

        return MaaBool.valueOf(result.getBox() != null);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class AnalyzeArg {

        private TaskDetail taskDetail;
        private String currentTaskName;
        private String customRecognitionName;
        private String customRecognitionParam;
        private BufferedImage image;
        private Rect roi;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class AnalyzeResult {

        private Rect box;
        private String detail;

    }

}
