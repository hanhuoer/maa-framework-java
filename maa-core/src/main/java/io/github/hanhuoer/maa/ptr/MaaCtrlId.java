package io.github.hanhuoer.maa.ptr;

import lombok.Getter;

/**
 * @author H
 */
@Getter
public class MaaCtrlId extends MaaId {

    public MaaCtrlId() {
        super();
    }

    public MaaCtrlId(long value) {
        super(value);
    }

    public static MaaCtrlId valueOf(long value) {
        return new MaaCtrlId(value);
    }

}
