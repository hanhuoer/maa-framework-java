package io.github.hanhuoer.maa.ptr;

import io.github.hanhuoer.maa.ptr.base.MaaLong;

/***
 * @author H
 */
public class MaaId extends MaaLong {

    public MaaId() {
        super();
    }

    public MaaId(long value) {
        super(value);
    }

    public static MaaId valueOf(long value) {
        return new MaaId(value);
    }

    public long getValue() {
        return this.longValue();
    }
}
