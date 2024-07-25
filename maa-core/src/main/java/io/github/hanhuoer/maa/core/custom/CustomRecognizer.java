package io.github.hanhuoer.maa.core.custom;

import io.github.hanhuoer.maa.ptr.*;
import io.github.hanhuoer.maa.model.*;
import io.github.hanhuoer.maa.ptr.StringBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author H
 */
@Getter
@Accessors(chain = true)
public abstract class CustomRecognizer {

    private final MaaCustomRecognizerHandle handle;

    public CustomRecognizer() {
        this(new MaaCustomRecognizerHandle());
    }

    private CustomRecognizer(MaaCustomRecognizerHandle handle) {
        this.handle = handle;
        handle.analyze = this::analyzeAgent;
    }

    /**
     * analyze the given image.
     *
     * @param context     the context.
     * @param image       the image to analyze.
     * @param taskName    the name of the task.
     * @param customParam the custom recognition param from pipeline.
     * @return analyze result object.
     */
    public abstract CustomRecognizerAnalyzeResult analyze(
            SyncContext context,
            BufferedImage image,
            String taskName,
            String customParam
    );


    public Boolean analyzeAgent(
            MaaSyncContextHandle contextHandle,
            MaaImageBufferHandle imageHandle,
            String taskName,
            String customParam,
            MaaTransparentArg arg,
            MaaRectHandle outBoxHandle,
            MaaStringBufferHandle outDetailHandle
    ) {
        SyncContext context = new SyncContext(contextHandle);
        ImageBuffer imageBuffer = new ImageBuffer(imageHandle);
        BufferedImage image = null;
        try {
            image = imageBuffer.getValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CustomRecognizerAnalyzeResult analyze = this.analyze(context, image, taskName, customParam);

        RectBuffer rectBuffer = new RectBuffer(outBoxHandle);
        rectBuffer.setValue(analyze.getBox());
        StringBuffer stringBuffer = new StringBuffer(outDetailHandle);
        stringBuffer.setValue(analyze.getDetail());

        return analyze.isSuccess();
    }

}
