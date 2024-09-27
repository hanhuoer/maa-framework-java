package io.github.hanhuoer.maa.define.base;

import io.github.hanhuoer.maa.define.NativeInteger;

public class MaaInteger extends NativeInteger {

    public MaaInteger() {
        super();
    }

    public MaaInteger(int value) {
        super(value);
    }

    public MaaInteger(int value, boolean unsigned) {
        super(value, unsigned);
    }

    public int getValue() {
        return this.intValue();
    }
}
