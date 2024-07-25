package io.github.hanhuoer.maa.consts;

import lombok.Getter;

/**
 * @author H
 */
@Getter
public enum MaaStatusEnum {

    INVALID(0),
    PENDING(1000),
    RUNNING(2000),
    SUCCESS(3000),
    FAILURE(4000);

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

}
