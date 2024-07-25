package io.github.hanhuoer.maa.core;

import io.github.hanhuoer.maa.callbak.MaaControllerCallback;
import io.github.hanhuoer.maa.core.base.Controller;
import io.github.hanhuoer.maa.core.custom.CustomControllerAgent;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.ptr.MaaCallbackTransparentArg;
import io.github.hanhuoer.maa.ptr.MaaControllerHandle;
import lombok.extern.slf4j.Slf4j;

/**
 * @author H
 */
@Slf4j
public class CustomController extends Controller {

    private CustomController() {
    }

    public CustomController(CustomControllerAgent customController) {
        this(customController, null, null);
    }

    public CustomController(CustomControllerAgent customController, MaaControllerCallback callback) {
        this(customController, callback, null);
    }

    public CustomController(CustomControllerAgent customController,
                            MaaControllerCallback callback, MaaCallbackTransparentArg callbackArg) {

        MaaControllerHandle handle = MaaFramework.controller().MaaCustomControllerCreate(
                customController.getHandle(),
                null,
                callback,
                callbackArg
        );

        if (handle == null) {
            throw new RuntimeException("CustomController create failed");
        }
        super.handle = handle;
    }

}
