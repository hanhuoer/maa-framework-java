package io.github.hanhuoer.maa.core;

import io.github.hanhuoer.maa.callbak.MaaControllerCallback;
import io.github.hanhuoer.maa.core.base.Controller;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.ptr.MaaControllerHandle;
import io.github.hanhuoer.maa.ptr.MaaThriftControllerType;
import io.github.hanhuoer.maa.ptr.MaaTransparentArg;
import lombok.extern.slf4j.Slf4j;

/**
 * @author H
 */
@Slf4j
public class ThriftController extends Controller {

    public ThriftController(Integer type, String host, int port, String config,
                            MaaControllerCallback callback, MaaTransparentArg callbackArg) {
        this(new MaaThriftControllerType(type), host, port, config,
                callback, callbackArg);
    }

    public ThriftController(MaaThriftControllerType type, String host, int port, String config,
                            MaaControllerCallback callback, MaaTransparentArg callbackArg) {
        super();

        MaaControllerHandle handle = MaaFramework.controller().MaaThriftControllerCreate(
                type,
                host, port, config,
                callback, null
        );

        if (handle == null) {
            throw new RuntimeException("ThriftController create failed");
        }
        super.handle = handle;
    }
}
