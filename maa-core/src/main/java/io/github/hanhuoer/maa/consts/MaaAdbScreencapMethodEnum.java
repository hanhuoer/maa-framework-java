package io.github.hanhuoer.maa.consts;

import io.github.hanhuoer.maa.define.MaaAdbScreencapMethod;
import lombok.Getter;

/**
 * Use bitwise OR to set the method you need
 * MaaFramework will test their speed and use the fastest one.
 *
 * @author H
 */
@Getter
public enum MaaAdbScreencapMethodEnum {

    NULL(0),
    ENCODE_TO_FILE_AND_PULL(1),
    ENCODE(1 << 1),
    RAW_WITH_GZIP(1 << 2),
    RAW_BY_NETCAT(1 << 3),
    MINICAP_DIRECT(1 << 4),
    MINICAP_STREAM(1 << 5),
    EMULATOR_EXTRAS(1 << 6),
    ALL(~NULL.value),
    DEFAULT(ALL.value & (~RAW_BY_NETCAT.value) & (~MINICAP_DIRECT.value)),
    ;

    private final long value;

    MaaAdbScreencapMethodEnum(long value) {
        this.value = value;
    }

    public static MaaAdbScreencapMethodEnum of(long method) {
        for (MaaAdbScreencapMethodEnum item : values()) {
            if (Long.valueOf(item.value).equals(method)) return item;
        }
        return DEFAULT;
    }

    public static MaaAdbScreencapMethodEnum of(MaaAdbScreencapMethod method) {
        return of(method.getValue());
    }

    public static MaaAdbScreencapMethodEnum valueOf(MaaAdbScreencapMethod method) {
        return of(method);
    }
}
