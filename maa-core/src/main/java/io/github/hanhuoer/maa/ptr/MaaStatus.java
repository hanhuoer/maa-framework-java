package io.github.hanhuoer.maa.ptr;

import io.github.hanhuoer.maa.consts.MaaStatusEnum;
import io.github.hanhuoer.maa.ptr.base.MaaLong;

/***
 * @author H
 */
public class MaaStatus extends MaaLong {

    public MaaStatus() {
        super();
    }

    public MaaStatus(int value) {
        super(value);
    }

    public static MaaStatus valueOf(int value) {
        return new MaaStatus(value);
    }

    public int getValue() {
        return this.intValue();
    }

    public MaaStatusEnum convert() {
        return MaaStatusEnum.of(this.getValue());
    }

}
