package io.github.hanhuoer.maa.consts;

import lombok.Getter;

/**
 * @author H
 */
@Getter
public enum MaaCtrlOptionEnum {
    INVALID(0),

    /**
     * Only one of long and short side can be set, and the other is automatically scaled according to the aspect ratio.
     * value: int, eg: 1920;
     */
    SCREENSHOT_TARGET_LONG_SIDE(1),

    /**
     * Only one of long and short side can be set, and the other is automatically scaled according to the aspect ratio.
     * value: int, eg: 1080;
     */
    SCREENSHOT_TARGET_SHORT_SIDE(2),

    /**
     * Screenshot use raw size without scaling.
     * Please note that this option may cause incorrect coordinates on user devices
     * with different resolutions if scaling is not performed.
     * value: bool, eg: true;
     */
    SCREENSHOT_USE_RAW_SIZE(3),

    /**
     * Dump all screenshots and actions
     * this option will || with MaaGlobalOptionEnum.Recording
     * value: bool, eg: true;
     */
    RECORDING(5),
    ;

    private final int value;

    MaaCtrlOptionEnum(int value) {
        this.value = value;
    }

}
