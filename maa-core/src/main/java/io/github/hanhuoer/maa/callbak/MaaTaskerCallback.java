package io.github.hanhuoer.maa.callbak;

import com.sun.jna.Callback;
import io.github.hanhuoer.maa.define.MaaCallbackTransparentArg;

/**
 * @author H
 */
public interface MaaTaskerCallback extends Callback {

    void callback(String message, String detailsJson, MaaCallbackTransparentArg callbackArg);

}
