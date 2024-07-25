package io.github.hanhuoer.maa.callbak;

import com.sun.jna.Callback;
import io.github.hanhuoer.maa.ptr.MaaCallbackTransparentArg;

/**
 * @author H
 */
public interface MaaAPICallback extends Callback {

    void callback(String msg, String detailsJson, MaaCallbackTransparentArg callbackArg);

}
