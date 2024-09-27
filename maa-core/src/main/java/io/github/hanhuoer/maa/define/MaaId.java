package io.github.hanhuoer.maa.define;

import io.github.hanhuoer.maa.define.base.MaaLong;

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
