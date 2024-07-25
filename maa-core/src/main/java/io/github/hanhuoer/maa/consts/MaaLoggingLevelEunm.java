package io.github.hanhuoer.maa.consts;

import lombok.Getter;

/**
 * @author H
 */
@Getter
public enum MaaLoggingLevelEunm {
    OFF(0),
    FATAL(1),
    ERROR(2),
    WARN(3),
    INFO(4),
    DEBUG(5),
    TRACE(6),
    ALL(7),
    ;

    private final int value;

    MaaLoggingLevelEunm(int value) {
        this.value = value;
    }

    public static MaaLoggingLevelEunm of(Integer status) {
        for (MaaLoggingLevelEunm item : values()) {
            if (Integer.valueOf(item.value).equals(status)) return item;
        }
        return null;
    }

}
