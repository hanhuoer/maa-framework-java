package io.github.hanhuoer.maa.consts;

import lombok.Getter;

/**
 * @author H
 */
@Getter
public enum MaaAdbControllerTypeEnum {

    INVALID(0),

    TOUCH_ADB(1),
    TOUCH_MINITOUCH(2),
    TOUCH_MAATOUCH(3),
    TOUCH_AUTODETECT(0xFF - 1),

    KEY_ADB(1 << 8),
    KEY_MAATOUCH(2 << 8),
    KEY_AUTODETECT(0xFF00 - (1 << 8)),

    INPUT_PRESET_ADB(TOUCH_ADB.value | KEY_ADB.value),
    INPUT_PRESET_MINITOUCH(TOUCH_MINITOUCH.value | KEY_ADB.value),
    INPUT_PRESET_MAATOUCH(TOUCH_MAATOUCH.value | KEY_MAATOUCH.value),
    INPUT_PRESET_AUTODETECT(TOUCH_AUTODETECT.value | KEY_AUTODETECT.value),

    SCREENCAP_RAWBYNETCAT(2 << 16),
    SCREENCAP_RAWWITHGZIP(3 << 16),
    SCREENCAP_ENCODE(4 << 16),
    SCREENCAP_ENCODETOFILE(5 << 16),
    SCREENCAP_MINICAPDIRECT(6 << 16),
    SCREENCAP_MINICAPSTREAM(7 << 16),
    SCREENCAP_FASTESTLOSSLESSWAY(0xFF0000 - (2 << 16)),
    SCREENCAP_FASTESTWAY(0xFF0000 - (1 << 16));

    public final int value;

    MaaAdbControllerTypeEnum(int value) {
        this.value = value;
    }

    public static MaaAdbControllerTypeEnum valueOf(int value) {
        for (MaaAdbControllerTypeEnum maaAdbControllerTypeEnum : values()) {
            if (maaAdbControllerTypeEnum.value == value) return maaAdbControllerTypeEnum;
        }
        return null;
    }

}
