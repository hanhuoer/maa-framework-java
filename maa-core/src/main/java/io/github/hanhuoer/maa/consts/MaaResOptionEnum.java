package io.github.hanhuoer.maa.consts;

import io.github.hanhuoer.maa.define.MaaResOption;
import lombok.Getter;

/**
 * @author H
 */
@Getter
public enum MaaResOptionEnum {
    INVALID(0),
    INFERENCE_DEVICE(1),
    ;

    private final int value;

    MaaResOptionEnum(int value) {
        this.value = value;
    }

    public MaaResOption value() {
        return new MaaResOption(this.value);
    }

}
