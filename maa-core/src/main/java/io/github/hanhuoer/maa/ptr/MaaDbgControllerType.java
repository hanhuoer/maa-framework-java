package io.github.hanhuoer.maa.ptr;

import com.sun.jna.ptr.LongByReference;
import io.github.hanhuoer.maa.consts.MaaDbgControllerTypeEnum;

/***
 * @author H
 */
public class MaaDbgControllerType extends LongByReference {

    public MaaDbgControllerType() {
        super();
    }

    public MaaDbgControllerType(long value) {
        super(value);
    }

    public static MaaDbgControllerType valueOf(MaaDbgControllerTypeEnum type) {
        return new MaaDbgControllerType(type.getValue());
    }
}
