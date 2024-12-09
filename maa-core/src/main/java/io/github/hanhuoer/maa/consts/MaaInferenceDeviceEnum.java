package io.github.hanhuoer.maa.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MaaInferenceDeviceEnum {

    CPU(-2),
    AUTO(-1),
    GPU0(0),
    GPU1(1),
    ;

    private final int value;

    public static MaaInferenceDeviceEnum of(Integer status) {
        for (MaaInferenceDeviceEnum item : values()) {
            if (Integer.valueOf(item.value).equals(status)) return item;
        }
        return null;
    }

}
