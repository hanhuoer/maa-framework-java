package io.github.hanhuoer.maa.consts;

import io.github.hanhuoer.maa.ptr.MaaGlobalOption;
import lombok.Getter;

/**
 * @author H
 */
@Getter
public enum MaaGlobalOptionEnum {
    INVALID(0),
    LOG_DIR(1),
    SAVE_DRAW(2),
    RECORDING(3),
    STDOUT_LEVEL(4),
    SHOW_HIT_DRAW(5),
    DEBUG_MODE(6),
    ;

    private final int value;

    MaaGlobalOptionEnum(int value) {
        this.value = value;
    }

    public MaaGlobalOption value() {
        return new MaaGlobalOption(this.value);
    }

}
