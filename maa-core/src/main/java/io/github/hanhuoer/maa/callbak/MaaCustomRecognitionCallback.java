package io.github.hanhuoer.maa.callbak;

import com.sun.jna.Callback;
import io.github.hanhuoer.maa.ptr.*;
import io.github.hanhuoer.maa.ptr.base.MaaBool;

/**
 * @author H
 */
public interface MaaCustomRecognitionCallback extends Callback {

    /**
     * @param outBox    output
     * @param outDetail output
     */
    MaaBool analyze(
            MaaContextHandle contextHandle,
            MaaTaskId maaTaskId,
            String currentTaskName,
            String customRecognitionName,
            String customRecognitionParam,
            MaaImageBufferHandle imageHandle,
            MaaRectHandle roi,
            MaaCallbackTransparentArg transparentArg,
            MaaRectHandle outBox,
            MaaStringBufferHandle outDetail
    );

}
