package io.github.hanhuoer.maa.consts;

import lombok.Getter;

/**
 * @author H
 */
@Getter
public enum MaaWin32ScreencapMethodEnum {

    NULL(0),
    GDI(1),
    FRAME_POOL(1 << 1),
    DXGI_DESKTOP_DUP(1 << 2),
    ;

    private final long value;

    MaaWin32ScreencapMethodEnum(long value) {
        this.value = value;
    }

    public static MaaWin32ScreencapMethodEnum of(long method) {
        for (MaaWin32ScreencapMethodEnum item : values()) {
            if (Long.valueOf(item.value).equals(method)) return item;
        }
        return NULL;
    }

    public static MaaWin32ScreencapMethodEnum of(MaaWin32ScreencapMethodEnum method) {
        return of(method.getValue());
    }

}
