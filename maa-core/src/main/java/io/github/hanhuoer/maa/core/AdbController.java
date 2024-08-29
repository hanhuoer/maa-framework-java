package io.github.hanhuoer.maa.core;

import com.alibaba.fastjson2.JSONObject;
import io.github.hanhuoer.maa.callbak.MaaControllerCallback;
import io.github.hanhuoer.maa.consts.MaaAdbControllerTypeEnum;
import io.github.hanhuoer.maa.core.base.Controller;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.jna.MaaToolkit;
import io.github.hanhuoer.maa.model.AdbInfo;
import io.github.hanhuoer.maa.ptr.MaaControllerHandle;
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

    public AdbController(AdbInfo info, MaaControllerCallback callback, Object callbackArgs) {
        this(info, null, callback, callbackArgs);
    }

    public AdbController(AdbInfo info, String agent, MaaControllerCallback callback, Object callbackArgs) {
        this(info.getPath(), info.getSerial(),
                MaaAdbControllerTypeEnum.valueOf(info.getControllerType()),
                null, null,
                Collections.emptyMap(),
                agent,
                callback,
                callbackArgs);
        this.adbInfo = info;
    }

    private AdbController(String adbPath, String address,
                          MaaAdbControllerTypeEnum touchType,
                          MaaAdbControllerTypeEnum keyType,
                          MaaAdbControllerTypeEnum screencapType,
                          Map<String, Object> config,
                          String agentPath,
                          MaaControllerCallback callback, Object callbackArgs) {
        super();

        if (touchType == null) touchType = MaaAdbControllerTypeEnum.TOUCH_AUTODETECT;
        if (keyType == null) keyType = MaaAdbControllerTypeEnum.KEY_AUTODETECT;
        if (screencapType == null) screencapType = MaaAdbControllerTypeEnum.SCREENCAP_FASTESTLOSSLESSWAY;

        MaaControllerHandle handle = MaaFramework.controller().MaaAdbControllerCreateV2(
                adbPath,
                address,
                touchType.value | keyType.value | screencapType.value,
                JSONObject.toJSONString(config),
                StringUtils.isEmpty(agentPath) ? agentPath() : agentPath,
                callback,
                null
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
     * @param adb adb path
     * @return 找到的设备列表, 否则返回一个空容器
     */
    public static List<AdbInfo> find(String adb) {
        Long count = 0L;
        if (StringUtils.isEmpty(adb)) {
            MaaToolkit.device().MaaToolkitPostFindDevice();
        } else {
            count = MaaToolkit.device().MaaToolkitFindDeviceWithAdb(adb);
        }
        count = MaaToolkit.device().MaaToolkitWaitForFindDeviceToComplete();

        if (count == null || count <= 0) {
            return Collections.emptyList();
        }

        return LongStream.range(0, count)
                .mapToObj(MaaToolkit::getAdbDevice)
                .collect(Collectors.toList());
    }

}
