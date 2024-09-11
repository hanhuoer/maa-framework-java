package io.github.hanhuoer.maa.core.base;

import com.sun.jna.Native;
import io.github.hanhuoer.maa.callbak.MaaControllerCallback;
import io.github.hanhuoer.maa.consts.MaaCtrlOptionEnum;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.ptr.ImageBuffer;
import io.github.hanhuoer.maa.ptr.MaaCallbackTransparentArg;
import io.github.hanhuoer.maa.ptr.MaaControllerHandle;
import io.github.hanhuoer.maa.ptr.StringBuffer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author H
 */
@Slf4j
public abstract class Controller implements AutoCloseable {

    protected ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Getter
    protected MaaControllerHandle handle;
    @Getter
    protected MaaControllerCallback callback;
    @Getter
    protected MaaCallbackTransparentArg callbackArgs;


    protected Controller() {
        this(null, null);
    }

    protected Controller(MaaControllerCallback callback, MaaCallbackTransparentArg callbackArgs) {
        this.callback = callback;
        this.callbackArgs = callbackArgs;
    }

    @Override
    public void close() {
        if (handle == null) return;
        MaaFramework.controller().MaaControllerDestroy(handle);
        handle = null;
        if (log.isDebugEnabled()) log.debug("controller has bean destroyed.");
    }

    public boolean connect() {
        return postConnect().join();
    }

    public CompletableFuture<Boolean> postConnect() {
        return CompletableFuture.supplyAsync(() -> {
            long ctrlId = MaaFramework.controller().MaaControllerPostConnection(handle);
            MaaFramework.controller().MaaControllerWait(handle, ctrlId);
            MaaFramework.controller().MaaControllerStatus(handle, ctrlId);
            return true;
        }, executor);
    }

    public boolean connected() {
        return MaaFramework.controller().MaaControllerConnected(handle);
    }

    public BufferedImage screencap() {
        return screencap(true);
    }

    public BufferedImage screencap(boolean capture) {
        Long ctrl;
        if (capture) {
            Boolean captured = postScreencap().join();
            if (!captured) {
                return null;
            }
        }

        ImageBuffer imageBuffer = new ImageBuffer();

        Boolean ret = MaaFramework.controller().MaaControllerGetImage(handle, imageBuffer.getHandle());
        if (!Boolean.TRUE.equals(ret)) {
            return null;
        }

        try {
            return imageBuffer.getValue();
        } catch (IOException e) {
            return null;
        } finally {
            imageBuffer.close();
        }
    }

    public CompletableFuture<Boolean> postScreencap() {
        return CompletableFuture.supplyAsync(() -> {
            Long ctrl = MaaFramework.controller().MaaControllerPostScreencap(handle);
            MaaFramework.controller().MaaControllerWait(handle, ctrl);
            return status(ctrl);
        }, executor);
    }

    public boolean click(int x, int y) {
        return postClick(x, y).join();
    }

    public CompletableFuture<Boolean> postClick(int x, int y) {
        return CompletableFuture.supplyAsync(() -> {
            Long ctrl = MaaFramework.controller().MaaControllerPostClick(handle, x, y);
            return status(ctrl);
        }, executor);
    }

    public boolean swipe(int x1, int y1, int x2, int y2, int duration) {
        return postSwipe(x1, y1, x2, y2, duration).join();
    }

    public CompletableFuture<Boolean> postSwipe(int x1, int y1, int x2, int y2, int duration) {
        return CompletableFuture.supplyAsync(() -> {
            Long ctrl = MaaFramework.controller().MaaControllerPostSwipe(handle, x1, y1, x2, y2, duration);
            return status(ctrl);
        }, executor);
    }

    public Controller setScreenshotTargetLongSide(int longSide) {
        MaaFramework.controller().MaaControllerSetOption(
                handle,
                MaaCtrlOptionEnum.SCREENSHOT_TARGET_LONG_SIDE.getValue(),
                longSide,
                Native.getNativeSize(int.class)
        );
        return this;
    }

    public Controller setScreenshotTargetShortSide(int shortSide) {
        MaaFramework.controller().MaaControllerSetOption(
                handle,
                MaaCtrlOptionEnum.SCREENSHOT_TARGET_SHORT_SIDE.getValue(),
                shortSide,
                Native.getNativeSize(int.class)
        );
        return this;
    }

    public boolean status(long maaId) {
        return MaaFramework.controller().MaaControllerStatus(handle, maaId) > 0;
    }

    public String uuid() {
        StringBuffer stringBuffer = new StringBuffer();
        MaaFramework.controller().MaaControllerGetUUID(this.handle, stringBuffer.getHandle());
        String uuid = stringBuffer.getValue();
        stringBuffer.close();
        return uuid;
    }

}
