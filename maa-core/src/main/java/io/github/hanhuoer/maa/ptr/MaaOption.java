package io.github.hanhuoer.maa.ptr;

import lombok.Getter;

/**
 * @author H
 */
@Getter
public class MaaOption extends NativeInteger {

    public MaaOption() {
        super();
    }

    public MaaOption(int value) {
        super(value);
    }

    public static MaaOption valueOf(int value) {
        return new MaaOption(value);
    }

}
