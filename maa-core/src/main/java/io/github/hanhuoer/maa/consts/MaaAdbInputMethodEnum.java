package io.github.hanhuoer.maa.consts;

import io.github.hanhuoer.maa.define.MaaAdbInputMethod;
import lombok.Getter;

/**
 * Use bitwise OR to set the method you need
 * MaaFramework will select the available ones according to priority.
 * The priority is: EmulatorExtras > Maatouch > MinitouchAndAdbKey > AdbShell
 *
 * @author H
 */
@Getter
public enum MaaAdbInputMethodEnum {

    NULL(0),
    ADB_SHELL(1),
    MINITOUCH_AND_ADB_KEY(1 << 1),
    MAATOUCH(1 << 2),
    EMULATOR_EXTRAS(1 << 3),
    ALL(~NULL.value),
    DEFAULT(ALL.value & (~EMULATOR_EXTRAS.value)),
    ;

    private final long value;

    MaaAdbInputMethodEnum(long value) {
        this.value = value;
    }

    public static MaaAdbInputMethodEnum of(long method) {
        for (MaaAdbInputMethodEnum item : values()) {
            if (Long.valueOf(item.value).equals(method)) return item;
        }
        return DEFAULT;
    }

    public static MaaAdbInputMethodEnum of(MaaAdbInputMethod method) {
        return of(method.getValue());
    }

    public static MaaAdbInputMethodEnum valueOf(MaaAdbInputMethod method) {
        return of(method);
    }
}
