package io.github.hanhuoer.maa.consts;

import lombok.Getter;

/**
 * @author H
 */
@Getter
public enum MaaWin32ControllerTypeEnum {
    INVALID(0),
    TOUCH_SENDMESSAGE(1),
    TOUCH_SEIZE(2),
    KEY_SENDMESSAGE(1 << 8),
    KEY_SEIZE(2 << 8),
    SCREENCAP_GDI(1 << 16),
    SCREENCAP_DXGI_DESKTOPDUP(2 << 16),
    SCREENCAP_DXGI_FRAMEPOOL(4 << 16),
    ;

    public final int value;

    MaaWin32ControllerTypeEnum(int value) {
        this.value = value;
    }

}
