package io.github.hanhuoer.maa.callbak;

import com.sun.jna.Callback;
import io.github.hanhuoer.maa.ptr.*;
import io.github.hanhuoer.maa.ptr.base.MaaBool;

/**
 * @author H
 */
public interface MaaCustomActionCallback {


    interface Run extends Callback {

        MaaBool callback(
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

}
