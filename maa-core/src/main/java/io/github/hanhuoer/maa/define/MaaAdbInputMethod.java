package io.github.hanhuoer.maa.define;

import com.sun.jna.NativeLong;
import io.github.hanhuoer.maa.consts.MaaAdbInputMethodEnum;

/***
 * @author H
 */
public class MaaAdbInputMethod extends NativeLong {

    public MaaAdbInputMethod() {
        super();
    }

    public MaaAdbInputMethod(long value) {
        super(value);
    }

    public static MaaAdbInputMethod valueOf(MaaAdbInputMethodEnum method) {
        return new MaaAdbInputMethod(method.getValue());
    }

    public long getValue() {
        return this.longValue();
    }

}
