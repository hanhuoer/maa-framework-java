package io.github.hanhuoer.maa.callbak;

import com.sun.jna.Callback;
import io.github.hanhuoer.maa.define.*;
import io.github.hanhuoer.maa.define.base.MaaBool;


/**
 * @author H
 */
public interface MaaCustomActionCallback extends Callback {

    MaaBool run(
            MaaContextHandle contextHandle,
            MaaTaskId taskId,
            String currentTaskName,
            String customActionName,
            String customActionParam,
            MaaRecoId recoId,
            MaaRectHandle boxHandle,
            MaaCallbackTransparentArg arg
    );

}
