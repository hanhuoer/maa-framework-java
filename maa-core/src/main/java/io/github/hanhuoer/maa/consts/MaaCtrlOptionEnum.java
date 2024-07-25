package io.github.hanhuoer.maa.consts;

import lombok.Getter;

/**
 * @author H
 */
@Getter
public enum MaaCtrlOptionEnum {
    INVALID(0),
    SCREENSHOT_TARGET_LONG_SIDE(1),
    SCREENSHOT_TARGET_SHORT_SIDE(2),
    DEFAULT_APP_PACKAGE_ENTRY(3),
    DEFAULT_APP_PACKAGE(4);

    private final int value;

    MaaCtrlOptionEnum(int value) {
        this.value = value;
    }

}
