package io.github.hanhuoer.maa.consts;

import io.github.hanhuoer.maa.define.MaaStatus;
import lombok.Getter;

/**
 * @author H
 */
@Getter
public enum MaaStatusEnum {

    INVALID(0),
    PENDING(1000),
    RUNNING(2000),
    SUCCEEDED(3000),
    FAILED(4000);

    private final int value;

    MaaStatusEnum(int value) {
        this.value = value;
    }

    public static MaaStatusEnum of(Integer status) {
        return of(status.longValue());
    }

    public static MaaStatusEnum of(MaaStatus status) {
        return of(status.getValue());
    }

    public static MaaStatusEnum of(Long status) {
        for (MaaStatusEnum item : values()) {
            if (Long.valueOf(item.value).equals(status)) return item;
        }
        return INVALID;
    }
}
