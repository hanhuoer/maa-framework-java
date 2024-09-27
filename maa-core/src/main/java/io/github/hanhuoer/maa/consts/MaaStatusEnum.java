package io.github.hanhuoer.maa.consts;

import io.github.hanhuoer.maa.define.MaaStatus;
import lombok.Getter;

import java.util.function.Function;

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
        for (MaaStatusEnum item : values()) {
            if (Integer.valueOf(item.value).equals(status)) return item;
        }
        return INVALID;
    }

    public static MaaStatusEnum of(MaaStatus status) {
        return of(status.getValue());
    }

    public static Function<Long, MaaStatusEnum> of(Object status) {
        return null;
    }
}
