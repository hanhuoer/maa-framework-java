package io.github.hanhuoer.maa.core.custom;

import com.sun.jna.ptr.IntByReference;
import io.github.hanhuoer.maa.ptr.StringBuffer;
import io.github.hanhuoer.maa.ptr.*;
import io.github.hanhuoer.maa.util.StringUtils;
import lombok.Getter;

import java.awt.image.BufferedImage;

/**
 * @author H
 */
@Getter
public abstract class CustomControllerAgent {

    private MaaCustomControllerHandle handle;


    public CustomControllerAgent() {
        this.handle = new MaaCustomControllerHandle();
        this.handle.connect = this::connectAgent;
        this.handle.requestUUID = this::requestUuidAgent;
        this.handle.startApp = this::startAppAgent;
        this.handle.stopApp = this::stopAppAgent;
        this.handle.screencap = this::screencapAgent;
        this.handle.click = this::clickAgent;
        this.handle.swipe = this::swipeAgent;
        this.handle.touchDown = this::touchDownAgent;
        this.handle.touchMove = this::touchMoveAgent;
        this.handle.touchUp = this::touchUpAgent;
        this.handle.pressKey = this::pressKeyAgent;
        this.handle.inputText = this::inputTextAgent;
    }

    public abstract boolean connect();

    public abstract String requestUuid();

    public abstract boolean startApp(String intent);

    public abstract boolean stopApp(String intent);

    public abstract BufferedImage screencap();

    public abstract boolean click(int x, int y);

    public abstract boolean swipe(int x1, int y1, int x2, int y2);

    public abstract boolean touchDown(int contact, int x, int y, int pressure);

    public abstract boolean touchMove(int contact, int x, int y, int pressure);

    public abstract boolean touchUp(int contact);

    public abstract boolean pressKey(int keycode);

    public abstract boolean inputText(String text);


    public boolean connectAgent(MaaTransparentArg arg) {
        return this.connect();
    }

    public boolean requestUuidAgent(MaaTransparentArg arg, MaaStringBufferHandle bufferHandle) {
        String uuid = this.requestUuid();
        if (StringUtils.isBlank(uuid)) return false;

        StringBuffer stringBuffer = new StringBuffer(bufferHandle);

        stringBuffer.setValue(uuid);
        return true;
    }

    public boolean startAppAgent(MaaStringView intent, MaaTransparentArg arg) {
        return this.startApp(intent.getValue());
    }

    public boolean stopAppAgent(MaaStringView intent, MaaTransparentArg arg) {
        return this.stopApp(intent.getValue());
    }

    public boolean screencapAgent(MaaTransparentArg arg, MaaImageBufferHandle bufferHandle) {
        BufferedImage screencap = this.screencap();

        if (screencap == null) return false;

        ImageBuffer imageBuffer = new ImageBuffer(bufferHandle);
        imageBuffer.setValue(screencap);

        return true;
    }

    public boolean clickAgent(IntByReference x, IntByReference y,
                              MaaTransparentArg arg) {
        return this.click(x.getValue(), y.getValue());
    }

    public boolean swipeAgent(IntByReference x1, IntByReference y1,
                              IntByReference x2, IntByReference y2,
                              IntByReference duration,
                              MaaTransparentArg arg) {
        return this.swipe(x1.getValue(), y1.getValue(), x2.getValue(), y2.getValue());
    }

    public boolean touchDownAgent(IntByReference contact,
                                  IntByReference x, IntByReference y,
                                  IntByReference pressure,
                                  MaaTransparentArg arg) {
        return this.touchDown(contact.getValue(), x.getValue(), y.getValue(), pressure.getValue());
    }

    public boolean touchMoveAgent(IntByReference contact,
                                  IntByReference x, IntByReference y,
                                  IntByReference pressure,
                                  MaaTransparentArg arg) {
        return this.touchMove(contact.getValue(), x.getValue(), y.getValue(), pressure.getValue());
    }

    public boolean touchUpAgent(IntByReference contact, MaaTransparentArg arg) {
        return this.touchUp(contact.getValue());
    }

    public boolean pressKeyAgent(IntByReference keycode, MaaTransparentArg arg) {
        return this.pressKey(keycode.getValue());
    }

    public boolean inputTextAgent(MaaStringView text, MaaTransparentArg arg) {
        return this.inputText(text.getValue());
    }

}
