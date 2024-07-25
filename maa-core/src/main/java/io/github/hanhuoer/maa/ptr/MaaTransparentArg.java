package io.github.hanhuoer.maa.ptr;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

/**
 * @author H
 */
public class MaaTransparentArg extends PointerType {

    public MaaTransparentArg() {
    }

    public MaaTransparentArg(Pointer p) {
        super(p);
    }
}
