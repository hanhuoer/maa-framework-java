package io.github.hanhuoer.maa.consts;

import lombok.Getter;

/**
 * @author H
 */
@Getter
public enum MaaDbgControllerTypeEnum {
    INVALID(0),
    CAROUSELIMAGE(1),
    REPLAYRECORDING(2),
    ;
    public final int value;

    MaaDbgControllerTypeEnum(int value) {
        this.value = value;
    }

}
