package io.github.hanhuoer.maa.define;

import com.sun.jna.ptr.LongByReference;
import io.github.hanhuoer.maa.consts.MaaWin32ScreencapMethodEnum;

/***
 * @author H
 */
public class MaaWin32ScreencapMethod extends LongByReference {

    public MaaWin32ScreencapMethod() {
        super();
    }

    public MaaWin32ScreencapMethod(long value) {
        super(value);
    }

    public static MaaWin32ScreencapMethod valueOf(MaaWin32ScreencapMethodEnum method) {
        return new MaaWin32ScreencapMethod(method.getValue());
    }

}
