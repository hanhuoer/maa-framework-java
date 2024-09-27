package io.github.hanhuoer.maa.define;

import com.sun.jna.ptr.LongByReference;
import io.github.hanhuoer.maa.consts.MaaWin32InputMethodEnum;

/***
 * @author H
 */
public class MaaWin32InputMethod extends LongByReference {

    public MaaWin32InputMethod() {
        super();
    }

    public MaaWin32InputMethod(long value) {
        super(value);
    }

    public static MaaWin32InputMethod valueOf(MaaWin32InputMethodEnum method) {
        return new MaaWin32InputMethod(method.getValue());
    }

}
