package io.github.hanhuoer.maa.consts;

import lombok.Getter;

/**
 * @author H
 */
@Getter
public enum MaaResOptionEnum {
    MaaResOption_Invalid(0),
    ;

    private final int value;

    MaaResOptionEnum(int value) {
        this.value = value;
    }

}
