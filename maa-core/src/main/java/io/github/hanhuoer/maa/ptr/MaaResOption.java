package io.github.hanhuoer.maa.ptr;

import lombok.Getter;

/**
 * @author H
 */
@Getter
public class MaaResOption extends MaaOption {

    public MaaResOption() {
        super();
    }

    public MaaResOption(int value) {
        super(value);
    }

    public static MaaResOption valueOf(int value) {
        return new MaaResOption(value);
    }

}
