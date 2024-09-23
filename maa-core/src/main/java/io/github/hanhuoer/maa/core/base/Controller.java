package io.github.hanhuoer.maa.core.base;

import com.sun.jna.Native;
import io.github.hanhuoer.maa.callbak.MaaNotificationCallback;
import io.github.hanhuoer.maa.consts.MaaCtrlOptionEnum;
import io.github.hanhuoer.maa.consts.MaaStatusEnum;
import io.github.hanhuoer.maa.core.util.Future;
import io.github.hanhuoer.maa.core.util.TaskFuture;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.ptr.StringBuffer;
import io.github.hanhuoer.maa.ptr.*;
import io.github.hanhuoer.maa.ptr.base.MaaBool;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

/**
 * @author H
 */
@Slf4j
@Getter
public class Controller implements AutoCloseable {

    protected MaaControllerHandle handle;
    private final Boolean own;
    protected MaaCallbackTransparentArg callbackArgs;
    protected MaaNotificationCallback callback;


    protected Controller() {
        this(null, null, null);
    }

    protected Controller(MaaControllerHandle handle) {
        this(handle, null, null);
    }

    protected Controller(MaaNotificationCallback callback, MaaCallbackTransparentArg callbackArgs) {
        this(null, callback, callbackArgs);
    }

    protected Controller(MaaControllerHandle handle, MaaNotificationCallback callback, MaaCallbackTransparentArg callbackArgs) {
        this.callback = callback;
        this.callbackArgs = callbackArgs;

        if (handle == null) {
            own = true;
        } else {
            this.handle = handle;
            own = false;
        }
    }

    @Override
    public void close() {
        if (handle == null) return;
        if (!own) return;
        MaaFramework.controller().MaaControllerDestroy(handle);
        handle = null;
        if (log.isDebugEnabled()) log.debug("controller has bean destroyed.");
    }

    public boolean connect() {
        return postConnection().waiting().succeeded();
    }

    public boolean connected() {
        return Optional.ofNullable(MaaFramework.controller().MaaControllerConnected(handle))
                .orElse(MaaBool.FALSE)
                .getValue();
    }

    public BufferedImage screencap() {
        return screencap(true);
    }

    public BufferedImage screencap(boolean capture) {
        Long ctrl;
        if (capture) {
            boolean captured = postScreencap().waiting().succeeded();
            if (!captured) {
                return null;
            }
        }

        ImageBuffer imageBuffer = new ImageBuffer();

        Boolean ret = MaaFramework.controller().MaaControllerCachedImage(handle, imageBuffer.getHandle()).getValue();
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

    public BufferedImage cachedImage() {
        return screencap(false);
    }

    public boolean click(int x, int y) {
        return postClick(x, y).waiting().succeeded();
    }

    public boolean swipe(int x1, int y1, int x2, int y2, int duration) {
        return postSwipe(x1, y1, x2, y2, duration).waiting().succeeded();
    }

    public Future postConnection() {
        MaaCtrlId ctrl = MaaFramework.controller().MaaControllerPostConnection(handle);
        return new Future(ctrl,
                id -> MaaStatusEnum.of(this.status(id.getValue())),
                id -> this.waiting(id.getValue()));
    }

    public Future postClick(int x, int y) {
        MaaCtrlId ctrl = MaaFramework.controller().MaaControllerPostClick(handle, x, y);
        return new Future(ctrl,
                id -> MaaStatusEnum.of(this.status(id.getValue())),
                id -> this.waiting(id.getValue()));
    }

    public Future postSwipe(int x1, int y1, int x2, int y2, int duration) {
        MaaCtrlId ctrl = MaaFramework.controller().MaaControllerPostSwipe(handle, x1, y1, x2, y2, duration);
        return new Future(ctrl,
                id -> MaaStatusEnum.of(this.status(id.getValue())),
                id -> this.waiting(id.getValue()));
    }

    public Future postPressKey(int keyCode) {
        MaaCtrlId ctrl = MaaFramework.controller().MaaControllerPostPressKey(handle, keyCode);
        return new Future(ctrl,
                id -> MaaStatusEnum.of(this.status(id.getValue())),
                id -> this.waiting(id.getValue()));
    }

    public Future postInputText(String text) {
        MaaCtrlId ctrl = MaaFramework.controller().MaaControllerPostInputText(handle, text);
        return new Future(ctrl,
                id -> MaaStatusEnum.of(this.status(id.getValue())),
                id -> this.waiting(id.getValue()));
    }

    public Future postStartApp(String intent) {
        MaaCtrlId ctrl = MaaFramework.controller().MaaControllerPostStartApp(handle, intent);
        return new Future(ctrl,
                id -> MaaStatusEnum.of(this.status(id.getValue())),
                id -> this.waiting(id.getValue()));
    }

    public Future postStopApp(String intent) {
        MaaCtrlId ctrl = MaaFramework.controller().MaaControllerPostStopApp(handle, intent);
        return new Future(ctrl,
                id -> MaaStatusEnum.of(this.status(id.getValue())),
                id -> this.waiting(id.getValue()));
    }

    public Future postTouchDown(int x, int y) {
        return postTouchDown(x, y, 0, 1);
    }

    public Future postTouchDown(int x, int y, int contact, int pressure) {
        MaaCtrlId ctrl = MaaFramework.controller().MaaControllerPostTouchDown(handle, x, y, contact, pressure);
        return new Future(ctrl,
                id -> MaaStatusEnum.of(this.status(id.getValue())),
                id -> this.waiting(id.getValue()));
    }

    public Future postTouchMove(int x, int y) {
        return postTouchMove(x, y, 0, 1);
    }

    public Future postTouchMove(int x, int y, int contact, int pressure) {
        MaaCtrlId ctrl = MaaFramework.controller().MaaControllerPostTouchMove(handle, x, y, contact, pressure);
        return new Future(ctrl,
                id -> MaaStatusEnum.of(this.status(id.getValue())),
                id -> this.waiting(id.getValue()));
    }

    public Future postTouchUp() {
        return postTouchUp(0);
    }

    public Future postTouchUp(int contact) {
        MaaCtrlId ctrl = MaaFramework.controller().MaaControllerPostTouchUp(handle, contact);
        return new Future(ctrl,
                id -> MaaStatusEnum.of(this.status(id.getValue())),
                id -> this.waiting(id.getValue()));
    }

    public TaskFuture<BufferedImage> postScreencap() {
        MaaCtrlId ctrl = MaaFramework.controller().MaaControllerPostScreencap(handle);
        return new TaskFuture<>(ctrl,
                id -> MaaStatusEnum.of(this.status(id.getValue())),
                id -> this.waiting(id.getValue()),
                id -> this.cachedImage());
    }

    public boolean setScreenshotTargetLongSide(int longSide) {
        return Boolean.TRUE.equals(MaaFramework.controller().MaaControllerSetOption(
                handle,
                new MaaCtrlOption(MaaCtrlOptionEnum.SCREENSHOT_TARGET_LONG_SIDE.getValue()),
                new MaaOptionValue(longSide),
                new MaaOptionValueSize(Native.getNativeSize(int.class))
        ).getValue());
    }

    public boolean setScreenshotTargetShortSide(int shortSide) {
        return Boolean.TRUE.equals(MaaFramework.controller().MaaControllerSetOption(
                handle,
                new MaaCtrlOption(MaaCtrlOptionEnum.SCREENSHOT_TARGET_SHORT_SIDE.getValue()),
                new MaaOptionValue(shortSide),
                new MaaOptionValueSize(Native.getNativeSize(int.class))
        ).getValue());
    }

    public String uuid() {
        StringBuffer stringBuffer = new StringBuffer();
        MaaBool maaBool = MaaFramework.controller().MaaControllerGetUUID(this.handle, stringBuffer.getHandle());
        if (!Boolean.TRUE.equals(maaBool.getValue())) {
            throw new RuntimeException("Failed to get UUID.");
        }
        String uuid = stringBuffer.getValue();
        stringBuffer.close();
        return uuid;
    }

    private MaaStatus status(long maaId) {
        return MaaFramework.controller().MaaControllerStatus(handle, new MaaCtrlId(maaId));
    }

    private MaaStatus waiting(long maaId) {
        return MaaFramework.controller().MaaControllerWait(handle, new MaaCtrlId(maaId));
    }

}
