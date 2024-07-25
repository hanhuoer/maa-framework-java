package io.github.hanhuoer.maa.callbak;

import com.sun.jna.Callback;
import io.github.hanhuoer.maa.ptr.*;

/**
 * @author H
 */
public interface MaaCustomRecognitionCallback extends Callback {

    /**
     * struct MaaCustomRecognizerAPI.MaaBool (*analyze)
     *
     * @param outBoxHandle    output
     * @param outDetailHandle output
     */
    Boolean analyze(
            MaaSyncContextHandle contextHandle,
            MaaImageBufferHandle imageHandle,
            String taskName,
            String customParam,
            MaaTransparentArg arg,
            MaaRectHandle outBoxHandle,
            MaaStringBufferHandle outDetailHandle
    );

}
