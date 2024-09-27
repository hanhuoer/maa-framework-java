package io.github.hanhuoer.maa.define;

import com.sun.jna.NativeLong;
import io.github.hanhuoer.maa.consts.MaaAdbScreencapMethodEnum;

/***
 * @author H
 */
public class MaaAdbScreencapMethod extends NativeLong {

    public MaaAdbScreencapMethod() {
        super();
    }

    public MaaAdbScreencapMethod(long value) {
        super(value);
    }

    public static MaaAdbScreencapMethod valueOf(MaaAdbScreencapMethodEnum method) {
        return new MaaAdbScreencapMethod(method.getValue());
    }

    public long getValue() {
        return this.longValue();
    }
}
