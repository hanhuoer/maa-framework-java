package io.github.hanhuoer.maa.core;

import com.alibaba.fastjson2.JSONObject;
import io.github.hanhuoer.maa.callbak.MaaNotificationCallback;
import io.github.hanhuoer.maa.consts.MaaAdbInputMethodEnum;
import io.github.hanhuoer.maa.consts.MaaAdbScreencapMethodEnum;
import io.github.hanhuoer.maa.core.base.Controller;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.jna.MaaToolkit;
import io.github.hanhuoer.maa.model.AdbInfo;
import io.github.hanhuoer.maa.ptr.*;
import io.github.hanhuoer.maa.ptr.base.MaaBool;
import io.github.hanhuoer.maa.util.FileUtils;
import io.github.hanhuoer.maa.util.StringUtils;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @author H
 */
public class AdbController extends Controller {

    @Getter
    private AdbInfo adbInfo;


    public AdbController(AdbInfo info) {
        this(info, null);
    }

    public AdbController(AdbInfo info, String agent) {
        this(info, agent, null, null);
    }

    public AdbController(AdbInfo info, MaaNotificationCallback callback, MaaCallbackTransparentArg callbackArgs) {
        this(info, null, callback, callbackArgs);
    }

    public AdbController(AdbInfo info, String agent, MaaNotificationCallback callback, MaaCallbackTransparentArg callbackArgs) {
        this(info.getPath(), info.getAddress(),
                MaaAdbScreencapMethodEnum.of(info.getScreencapMethod()),
                MaaAdbInputMethodEnum.of(info.getInputMethod()),
                Collections.emptyMap(),
                agent,
                callback,
                callbackArgs);
        this.adbInfo = info;
    }

    private AdbController(String adbPath, String address,
                          MaaAdbScreencapMethodEnum screencapMethod,
                          MaaAdbInputMethodEnum inputMethod,
                          Map<String, Object> config,
                          String agentPath,
                          MaaNotificationCallback callback, MaaCallbackTransparentArg callbackArgs) {
        super();

        this.callback = callback;
        this.callbackArgs = callbackArgs;

        if (screencapMethod == null) screencapMethod = MaaAdbScreencapMethodEnum.DEFAULT;
        if (inputMethod == null) inputMethod = MaaAdbInputMethodEnum.DEFAULT;

        MaaControllerHandle handle = MaaFramework.controller().MaaAdbControllerCreate(
                adbPath, address,
                MaaAdbScreencapMethod.valueOf(screencapMethod),
                MaaAdbInputMethod.valueOf(inputMethod),
                JSONObject.toJSONString(config),
                StringUtils.isEmpty(agentPath) ? agentPath() : agentPath,
                callback, callbackArgs
        );

        if (handle == null) {
            throw new RuntimeException("AdbController create failed");
        }
        super.handle = handle;
    }

    public static String agentPath() {
        return FileUtils.joinUserDir("resources", "maa", "agent").getAbsolutePath();
    }


    public static List<AdbInfo> find() {
        return find(null);
    }

    /**
     * 查找设备
     *
     * @param adbPath adb path
     * @return 找到的设备列表, 否则返回一个空容器
     */
    public static List<AdbInfo> find(String adbPath) {
        long count = 0L;
        MaaToolkitAdbDeviceListHandle buffer = MaaToolkit.device().MaaToolkitAdbDeviceListCreate();
        MaaBool maaBool = MaaBool.FALSE;
        if (StringUtils.isEmpty(adbPath)) {
            maaBool = MaaToolkit.device().MaaToolkitAdbDeviceFind(buffer);
        } else {
            maaBool = MaaToolkit.device().MaaToolkitAdbDeviceFindSpecified(adbPath, buffer);
        }

        try {
            if (Boolean.FALSE.equals(maaBool.getValue())) {
                return Collections.emptyList();
            }
            MaaSize maaSize = MaaToolkit.device().MaaToolkitAdbDeviceListSize(buffer);
            count = maaSize.getValue();
            if (count <= 0) {
                return Collections.emptyList();
            }

            return LongStream.range(0, count)
                    .mapToObj(i -> MaaToolkit.device().MaaToolkitAdbDeviceListAt(buffer, MaaSize.valueOf(i)))
                    .map(AdbController::getAdbDevice)
                    .collect(Collectors.toList());
        } finally {
            MaaToolkit.device().MaaToolkitAdbDeviceListDestroy(buffer);
        }
    }

    private static AdbInfo getAdbDevice(MaaToolkitAdbDeviceHandle handle) {
        String deviceAdbPath = MaaToolkit.device().MaaToolkitAdbDeviceGetAdbPath(handle);
        String deviceAdbConfig = MaaToolkit.device().MaaToolkitAdbDeviceGetConfig(handle);
        String deviceName = MaaToolkit.device().MaaToolkitAdbDeviceGetName(handle);

        String deviceAddress = MaaToolkit.device().MaaToolkitAdbDeviceGetAddress(handle);
        MaaAdbInputMethod maaAdbInputMethod = MaaToolkit.device().MaaToolkitAdbDeviceGetInputMethods(handle);
        MaaAdbScreencapMethod maaAdbScreencapMethod = MaaToolkit.device().MaaToolkitAdbDeviceGetScreencapMethods(handle);


        return new AdbInfo()
                .setDevice(handle)
                .setPath(deviceAdbPath)
                .setConfig(deviceAdbConfig)
                .setName(deviceName)
                .setAddress(deviceAddress)
                .setInputMethod(maaAdbInputMethod)
                .setScreencapMethod(maaAdbScreencapMethod);
    }

}
