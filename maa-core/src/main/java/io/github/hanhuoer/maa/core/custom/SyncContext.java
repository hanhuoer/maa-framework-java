package io.github.hanhuoer.maa.core.custom;

import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.model.Rect;
import io.github.hanhuoer.maa.ptr.StringBuffer;
import io.github.hanhuoer.maa.ptr.*;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author H
 */
@Getter
public class SyncContext {

    private final MaaSyncContextHandle handle;

    public SyncContext(MaaSyncContextHandle handle) {
        if (handle == null) {
            throw new RuntimeException("Context handle cannot be null");
        }
        this.handle = handle;
    }

    public boolean runTask(String taskName) {
        return runTask(taskName, "{}");
    }

    /**
     * sync context run task.
     *
     * @param taskName  task name
     * @param taskParam task param
     * @return tru if success
     */
    public boolean runTask(String taskName, String taskParam) {
        return Boolean.TRUE.equals(
                MaaFramework.syncContext().MaaSyncContextRunTask(
                        this.handle,
                        taskName,
                        taskParam
                )
        );
    }

    public RecognitionResult runRecognition(BufferedImage image, String taskName) {
        return runRecognition(image, taskName, "{}");
    }

    /**
     * sync context run recognizer.
     *
     * @param image     image
     * @param taskName  task name
     * @param taskParam task param
     * @return rect, detail
     */
    public RecognitionResult runRecognition(BufferedImage image, String taskName, String taskParam) {
        ImageBuffer imageBuffer = new ImageBuffer();
        imageBuffer.setValue(image);

        RectBuffer rectBuffer = new RectBuffer();
        StringBuffer detailBuffer = new StringBuffer();

        Boolean maaBool = MaaFramework.syncContext().MaaSyncContextRunRecognition(
                this.handle,
                imageBuffer.getHandle(),
                taskName,
                taskParam,
                rectBuffer.getHandle(),
                detailBuffer.getHandle()
        );

        RecognitionResult recognitionResult = new RecognitionResult()
                .setSuccess(Boolean.TRUE.equals(maaBool))
                .setRect(rectBuffer.getValue())
                .setDetail(detailBuffer.getValue());

        detailBuffer.close();

        return recognitionResult;
    }

    public boolean runAction(String taskName, Rect curBox, String curRecDetail) {
        return runAction(taskName, "{}", curBox, curRecDetail);
    }

    /**
     * sync context run action.
     *
     * @param taskName     task name
     * @param taskParam    task param
     * @param curBox       current box
     * @param curRecDetail current recognizer detail
     * @return detail
     */
    public boolean runAction(String taskName, String taskParam, Rect curBox, String curRecDetail) {
        RectBuffer rectBuffer = new RectBuffer();
        rectBuffer.setValue(curBox);

        Boolean maaBool = MaaFramework.syncContext().MaaSyncContextRunAction(
                this.handle,
                taskName,
                taskParam,
                rectBuffer.getHandle(),
                curRecDetail
        );

        rectBuffer.close();

        return Boolean.TRUE.equals(maaBool);
    }

    public boolean click(int x, int y) {
        return Boolean.TRUE.equals(
                MaaFramework.syncContext().MaaSyncContextClick(
                        this.handle,
                        x,
                        y
                )
        );
    }

    public boolean swipe(int x1, int y1, int x2, int y2) {
        return swipe(x1, y1, x2, y2, 1000);
    }

    public boolean swipe(int x1, int y1, int x2, int y2, int duration) {
        return Boolean.TRUE.equals(
                MaaFramework.syncContext().MaaSyncContextSwipe(
                        this.handle, x1, y1, x2, y2, duration
                )
        );
    }

    public boolean pressKey(int key) {
        return Boolean.TRUE.equals(
                MaaFramework.syncContext().MaaSyncContextPressKey(
                        this.handle, key
                )
        );
    }

    public boolean inputText(String text) {
        return Boolean.TRUE.equals(
                MaaFramework.syncContext().MaaSyncContextInputText(
                        this.handle, text
                )
        );
    }

    public boolean touchDown(int contact, int x, int y, int pressure) {
        return Boolean.TRUE.equals(
                MaaFramework.syncContext().MaaSyncContextTouchDown(
                        this.handle, contact, x, y, pressure
                )
        );
    }

    public boolean touchMove(int contact, int x, int y, int pressure) {
        return Boolean.TRUE.equals(
                MaaFramework.syncContext().MaaSyncContextTouchMove(
                        this.handle, contact, x, y, pressure
                )
        );
    }

    public boolean touchUp(int contact) {
        return Boolean.TRUE.equals(
                MaaFramework.syncContext().MaaSyncContextTouchUp(
                        this.handle, contact
                )
        );
    }

    public BufferedImage screencap() throws IOException {
        ImageBuffer imageBuffer = new ImageBuffer();
        Boolean maaBool = MaaFramework.syncContext().MaaSyncContextScreencap(
                this.handle,
                imageBuffer.getHandle()
        );

        BufferedImage image = imageBuffer.getValue();

        if (!Boolean.TRUE.equals(maaBool)) return null;
        return image;
    }

    public BufferedImage cachedImage() throws IOException {
        ImageBuffer imageBuffer = new ImageBuffer();
        Boolean maaBool = MaaFramework.syncContext().MaaSyncContextCachedImage(
                this.handle,
                imageBuffer.getHandle()
        );

        BufferedImage image = imageBuffer.getValue();
        if (!Boolean.TRUE.equals(maaBool)) return null;
        return image;
    }

    @Data
    @Accessors(chain = true)
    public static class RecognitionResult {
        private boolean success;
        private Rect rect;
        private String detail;
    }

}