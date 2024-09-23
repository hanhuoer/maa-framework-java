package io.github.hanhuoer.maa.consts;

import lombok.Getter;

/**
 * @author H
 */
@Getter
public enum MaaWin32InputMethodEnum {

    NULL(0),
    SEIZE(1),
    SEND_MESSAGE(1 << 1),
    ;

    private final long value;

    MaaWin32InputMethodEnum(long value) {
        this.value = value;
    }

    public static MaaWin32InputMethodEnum of(long method) {
        for (MaaWin32InputMethodEnum item : values()) {
            if (Long.valueOf(item.value).equals(method)) return item;
        }
        return NULL;
    }

    public static MaaWin32InputMethodEnum of(MaaWin32InputMethodEnum method) {
        return of(method.getValue());
    }

}
