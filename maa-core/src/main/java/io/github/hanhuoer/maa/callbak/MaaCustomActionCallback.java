package io.github.hanhuoer.maa.callbak;

import com.sun.jna.Callback;
import io.github.hanhuoer.maa.ptr.MaaRectHandle;
import io.github.hanhuoer.maa.ptr.MaaSyncContextHandle;
import io.github.hanhuoer.maa.ptr.MaaTransparentArg;

/**
 * @author H
 */
public interface MaaCustomActionCallback {


    interface Run extends Callback {

        Boolean callback(
                MaaSyncContextHandle contextHandle,
                String taskName,
                String customParam,
                MaaRectHandle boxHandle,
                String recDetail,
                MaaTransparentArg arg
        );

    }

    interface Stop extends Callback {

        void callback();

    }

}
