package io.github.hanhuoer.maa.core;

import io.github.hanhuoer.maa.callbak.MaaControllerCallback;
import io.github.hanhuoer.maa.consts.MaaDbgControllerTypeEnum;
import io.github.hanhuoer.maa.core.base.Controller;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.ptr.MaaControllerHandle;
import lombok.extern.slf4j.Slf4j;

/**
 * @author H
 */
@Slf4j
public class DbgController extends Controller {

    public DbgController(String readPath, String writePath, String config,
                         MaaDbgControllerTypeEnum touchType,
                         MaaDbgControllerTypeEnum keyType,
                         MaaDbgControllerTypeEnum screencapType,
                         MaaControllerCallback callback, Object callbackArgs) {
        super();

        if (touchType == null) touchType = MaaDbgControllerTypeEnum.INVALID;
        if (keyType == null) keyType = MaaDbgControllerTypeEnum.INVALID;
        if (screencapType == null) screencapType = MaaDbgControllerTypeEnum.CAROUSELIMAGE;

        MaaControllerHandle handle = MaaFramework.controller().MaaDbgControllerCreate(
                readPath, writePath,
                touchType.value | keyType.value | screencapType.value,
                config, callback, null
        );


        if (handle == null) {
            throw new RuntimeException("DbgController create failed");
        }
        super.handle = handle;
    }
}
