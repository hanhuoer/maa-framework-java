package io.github.hanhuoer.maa.core;

import io.github.hanhuoer.maa.buffer.ImageBuffer;
import io.github.hanhuoer.maa.buffer.StringBuffer;
import io.github.hanhuoer.maa.callbak.MaaNotificationCallback;
import io.github.hanhuoer.maa.core.base.Controller;
import io.github.hanhuoer.maa.define.*;
import io.github.hanhuoer.maa.define.base.MaaInteger;
import io.github.hanhuoer.maa.exception.ControllerCreateException;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * @author H
 */
@Slf4j
public abstract class CustomController extends Controller {

    private MaaCustomControllerHandle callback;

    public CustomController() {
        this(null, null, null);
    }

    public CustomController(MaaCustomControllerHandle callback) {
        this(callback, null, null);
    }

    public CustomController(MaaCustomControllerHandle callback, MaaNotificationCallback notificationCallback) {
        this(callback, notificationCallback, null);
    }

    public CustomController(MaaCustomControllerHandle callback,
                            MaaNotificationCallback notificationCallback,
                            MaaCallbackTransparentArg callbackArg) {

        this.callback = Objects.requireNonNullElseGet(callback, MaaCustomControllerHandle::new);
        this.callback.connect = this::connectAgent;
        this.callback.requestUUID = this::requestUuidAgent;
        this.callback.startApp = this::startAppAgent;
        this.callback.stopApp = this::stopAppAgent;
        this.callback.screencap = this::screencapAgent;
        this.callback.click = this::clickAgent;
        this.callback.swipe = this::swipeAgent;
        this.callback.touchDown = this::touchDownAgent;
        this.callback.touchMove = this::touchMoveAgent;
        this.callback.touchUp = this::touchUpAgent;
        this.callback.pressKey = this::pressKeyAgent;
        this.callback.inputText = this::inputTextAgent;

        MaaControllerHandle controllerHandle = MaaFramework.controller().MaaCustomControllerCreate(
                this.callback,
                null,
                notificationCallback,
                callbackArg
        );

        if (controllerHandle == null) {
            throw new ControllerCreateException("CustomController create failed");
        }
        super.handle = controllerHandle;
    }

    public abstract boolean connect();

    public abstract String requestUuid();

    public abstract boolean startApp(String intent);

    public abstract boolean stopApp(String intent);

    public abstract BufferedImage screencap();

    public abstract boolean click(int x, int y);

    public abstract boolean swipe(int x1, int y1, int x2, int y2, int duration);

    public abstract boolean touchDown(int contact, int x, int y, int pressure);

    public abstract boolean touchMove(int contact, int x, int y, int pressure);

    public abstract boolean touchUp(int contact);

    public abstract boolean pressKey(int keycode);

    public abstract boolean inputText(String text);

    public boolean connectAgent(MaaTransparentArg arg) {
        log.debug("connect agent;");
        return this.connect();
    }

    public boolean requestUuidAgent(MaaTransparentArg arg, MaaStringBufferHandle bufferHandle) {
        log.debug("request uuid agent;");
        String uuid = this.requestUuid();
        if (StringUtils.isBlank(uuid)) return false;

        StringBuffer stringBuffer = new StringBuffer(bufferHandle);
        stringBuffer.setValue(uuid);
        return true;
    }

    public boolean startAppAgent(MaaStringView intent, MaaTransparentArg arg) {
        log.debug("start app agent; intent: {}", intent);
        return this.startApp(intent.getValue());
    }

    public boolean stopAppAgent(MaaStringView intent, MaaTransparentArg arg) {
        log.debug("stop app agent; intent: {}", intent);
        return this.stopApp(intent.getValue());
    }

    public boolean screencapAgent(MaaTransparentArg arg, MaaImageBufferHandle bufferHandle) {
        log.debug("screencap agent;");
        BufferedImage screencap = this.screencap();

        if (screencap == null) return false;

        ImageBuffer imageBuffer = new ImageBuffer(bufferHandle);
        imageBuffer.setValue(screencap);

        return true;
    }

    public boolean clickAgent(MaaInteger x, MaaInteger y,
                              MaaTransparentArg arg) {
        log.debug("click agent; x: {}, y: {}", x, y);
        return this.click(x.getValue(), y.getValue());
    }

    public boolean swipeAgent(MaaInteger x1, MaaInteger y1,
                              MaaInteger x2, MaaInteger y2,
                              MaaInteger duration,
                              MaaTransparentArg arg) {
        log.debug("swipe agent; x1: {}, y1: {}, x2: {}, y2: {}, duration: {}", x1, y1, x2, y2, duration);
        return this.swipe(x1.getValue(), y1.getValue(), x2.getValue(), y2.getValue(), duration.getValue());
    }

    public boolean touchDownAgent(MaaInteger contact,
                                  MaaInteger x, MaaInteger y,
                                  MaaInteger pressure,
                                  MaaTransparentArg arg) {
        log.debug("touch down agent; contact: {}, x: {}, y: {}, pressure: {}", contact, x, y, pressure);
        return this.touchDown(contact.getValue(), x.getValue(), y.getValue(), pressure.getValue());
    }

    public boolean touchMoveAgent(MaaInteger contact,
                                  MaaInteger x, MaaInteger y,
                                  MaaInteger pressure,
                                  MaaTransparentArg arg) {
        log.debug("touch move agent; contact: {}, x: {}, y: {}, pressure: {}", contact, x, y, pressure);
        return this.touchMove(contact.getValue(), x.getValue(), y.getValue(), pressure.getValue());
    }

    public boolean touchUpAgent(MaaInteger contact, MaaTransparentArg arg) {
        log.debug("touch up agent; contact: {}", contact);
        return this.touchUp(contact.getValue());
    }

    public boolean pressKeyAgent(MaaInteger keycode, MaaTransparentArg arg) {
        log.debug("press key agent; keycode: {}", keycode);
        return this.pressKey(keycode.getValue());
    }

    public boolean inputTextAgent(MaaStringView text, MaaTransparentArg arg) {
        log.debug("input text agent; text: {}", text);
        return this.inputText(text.getValue());
    }

}
