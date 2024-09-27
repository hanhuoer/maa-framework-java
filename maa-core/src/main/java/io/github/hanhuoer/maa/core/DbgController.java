package io.github.hanhuoer.maa.core;

import io.github.hanhuoer.maa.callbak.MaaNotificationCallback;
import io.github.hanhuoer.maa.consts.MaaDbgControllerTypeEnum;
import io.github.hanhuoer.maa.core.base.Controller;
import io.github.hanhuoer.maa.define.MaaCallbackTransparentArg;
import io.github.hanhuoer.maa.define.MaaControllerHandle;
import io.github.hanhuoer.maa.define.MaaDbgControllerType;
import io.github.hanhuoer.maa.jna.MaaFramework;
import lombok.extern.slf4j.Slf4j;

/**
 * @author H
 */
@Slf4j
public class DbgController extends Controller {

    public DbgController(String readPath, String writePath, String config,
                         MaaDbgControllerTypeEnum dbgType,
                         MaaNotificationCallback callback, MaaCallbackTransparentArg callbackArgs) {
        super();

        MaaControllerHandle handle = MaaFramework.controller().MaaDbgControllerCreate(
                readPath, writePath,
                MaaDbgControllerType.valueOf(dbgType),
                config, callback, callbackArgs
        );


        if (handle == null) {
            throw new RuntimeException("Failed to create DbgController.");
        }
        super.handle = handle;
    }
}
