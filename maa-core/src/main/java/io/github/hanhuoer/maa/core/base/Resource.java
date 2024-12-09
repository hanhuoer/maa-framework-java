package io.github.hanhuoer.maa.core.base;

import io.github.hanhuoer.maa.buffer.StringBuffer;
import io.github.hanhuoer.maa.callbak.MaaNotificationCallback;
import io.github.hanhuoer.maa.consts.MaaInferenceDeviceEnum;
import io.github.hanhuoer.maa.consts.MaaInferenceExecutionProviderEnum;
import io.github.hanhuoer.maa.consts.MaaResOptionEnum;
import io.github.hanhuoer.maa.consts.MaaStatusEnum;
import io.github.hanhuoer.maa.core.custom.CustomAction;
import io.github.hanhuoer.maa.core.custom.CustomRecognition;
import io.github.hanhuoer.maa.core.util.Future;
import io.github.hanhuoer.maa.define.*;
import io.github.hanhuoer.maa.define.base.MaaBool;
import io.github.hanhuoer.maa.jna.MaaFramework;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author H
 */
@Slf4j
public class Resource implements AutoCloseable {

    @Getter
    private final boolean own;
    @Getter
    private MaaResourceHandle handle;
    @Getter
    private final MaaNotificationCallback callback;
    @Getter
    private final MaaCallbackTransparentArg callbackArg;
    private final Map<String, CustomRecognition> customRecognitionMap;
    private final Map<String, CustomAction> customActionHashMap;


    public Resource() {
        this(null, null, null);
    }

    public Resource(MaaResourceHandle handle) {
        this(handle, null, null);
    }

    public Resource(MaaNotificationCallback callback, MaaCallbackTransparentArg callbackArg) {
        this(null, callback, callbackArg);
    }

    public Resource(MaaResourceHandle handle, MaaNotificationCallback callback, MaaCallbackTransparentArg callbackArg) {
        this.callback = callback;
        this.callbackArg = callbackArg;

        if (handle == null) {
            own = true;
            this.handle = MaaFramework.resource().MaaResourceCreate(callback, callbackArg);

        } else {
            own = false;
            this.handle = handle;
        }

        if (this.handle == null) {
            throw new RuntimeException("Failed to create resource.");
        }

        this.customRecognitionMap = new HashMap<>();
        this.customActionHashMap = new HashMap<>();
    }

    @Override
    public void close() {
        if (this.handle == null) return;
        if (!this.own) return;
        MaaFramework.resource().MaaResourceDestroy(this.handle);
        this.handle = null;
        if (log.isDebugEnabled()) log.debug("resource has bean destroyed.");
    }

    public Boolean load(String path) {
        Future future = this.postPath(path);
        return future.waiting().done();
    }

    public Future loadAsync(String path) {
        return this.postPath(path);
    }

    public Future postPath(String path) {
        MaaId maaId = MaaFramework.resource().MaaResourcePostPath(this.handle, path);
        Function<MaaId, MaaStatusEnum> statusFunc = id -> MaaStatusEnum.of(this.status(id.getValue()));
        Function<MaaId, Void> waitFunc = id -> {
            this.waiting(id.getValue());
            return null;
        };

        return new Future(maaId, statusFunc, waitFunc);
    }

    public boolean loaded() {
        return MaaBool.TRUE.equals(
                MaaFramework.resource().MaaResourceLoaded(this.handle)
        );
    }

    public boolean clear() {
        return MaaBool.TRUE.equals(
                MaaFramework.resource().MaaResourceClear(this.handle)
        );
    }

    @Deprecated
    public boolean registerRecognizer(CustomRecognition recognizer) {
        if (recognizer == null) {
            throw new IllegalArgumentException("Recognition cannot be null");
        }
        return registerRecognizer(recognizer.getName(), recognizer);
    }

    @Deprecated
    public boolean registerRecognizer(String name, CustomRecognition recognizer) {
        return registerRecognition(name, recognizer);
    }

    public boolean registerRecognition(CustomRecognition recognizer) {
        if (recognizer == null) {
            throw new IllegalArgumentException("Recognition cannot be null");
        }
        return registerRecognition(recognizer.getName(), recognizer);
    }

    /**
     * register a custom recognizer.
     *
     * @param name       the name of the custom recognizer.
     * @param recognizer the custom recognizer.
     */
    public boolean registerRecognition(String name, CustomRecognition recognizer) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Recognition name cannot be null or empty");
        }
        if (recognizer == null) {
            throw new IllegalArgumentException("Recognition cannot be null");
        }

        MaaBool maaBool = MaaFramework.resource().MaaResourceRegisterCustomRecognition(
                this.handle,
                name,
                recognizer,
                null
        );

        boolean equals = MaaBool.TRUE.equals(maaBool);
        if (equals) {
            customRecognitionMap.put(name, recognizer);
        }
        return equals;
    }

    public boolean registerAction(CustomAction action) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }
        return registerAction(action.getName(), action);
    }

    public boolean registerAction(String name, CustomAction action) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Action name cannot be null or empty");
        }
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }

        MaaBool result = MaaFramework.resource().MaaResourceRegisterCustomAction(
                this.handle,
                name,
                action,
                null
        );

        boolean equals = MaaBool.TRUE.equals(result);
        if (equals) {
            customActionHashMap.put(name, action);
        }
        return equals;
    }

    public boolean unregister(CustomRecognition recognition) {
        if (recognition == null) {
            throw new IllegalArgumentException("Recognition cannot be null");
        }
        return unregisterAction(recognition.getName());
    }

    public boolean unregister(CustomAction action) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }
        return unregisterAction(action.getName());
    }

    public boolean unregisterRecognition(String name) {
        return unregisterRecognizer(name);
    }

    @Deprecated
    public boolean unregisterRecognizer(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Recognition name cannot be null or empty");
        }

        MaaBool result = MaaFramework.resource().MaaResourceUnregisterCustomRecognition(
                this.handle,
                name
        );

        boolean equals = MaaBool.TRUE.equals(result);
        if (equals) {
            customRecognitionMap.remove(name);
        }
        return equals;
    }

    public boolean unregisterAction(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Action name cannot be null or empty");
        }

        MaaBool result = MaaFramework.resource().MaaResourceUnregisterCustomAction(
                this.handle,
                name
        );

        boolean equals = MaaBool.TRUE.equals(result);
        if (equals) {
            customActionHashMap.remove(name);
        }
        return equals;
    }

    @Deprecated
    public boolean clearCustomRecognizer() {
        return clearCustomRecognition();
    }

    public boolean clearCustomRecognition() {
        MaaBool result = MaaFramework.resource().MaaResourceClearCustomRecognition(this.handle);

        boolean equals = MaaBool.TRUE.equals(result);
        if (equals) {
            customRecognitionMap.clear();
        }
        return equals;
    }

    public boolean clearCustomAction() {
        MaaBool result = MaaFramework.resource().MaaResourceClearCustomAction(this.handle);

        boolean equals = MaaBool.TRUE.equals(result);
        if (equals) {
            customActionHashMap.clear();
        }
        return equals;
    }

    public String hash() {
        try (StringBuffer buffer = new StringBuffer()) {
            MaaBool bool = MaaFramework.resource().MaaResourceGetHash(handle, buffer.getHandle());
            if (!MaaBool.TRUE.equals(bool)) {
                throw new RuntimeException("Failed to get hash.");
            }
            return buffer.getValue();
        }
    }

    public boolean useCpu() {
        return setInterfaceDevice(
                MaaInferenceExecutionProviderEnum.CPU,
                MaaInferenceDeviceEnum.CPU
        );
    }

    public boolean useDirectMl() {
        return useDirectMl(null);
    }

    public boolean useDirectMl(Integer deviceId) {
        if (deviceId == null) {
            deviceId = MaaInferenceDeviceEnum.AUTO.getValue();
        }
        return setInterfaceDevice(
                MaaInferenceExecutionProviderEnum.DIRECT_ML,
                deviceId
        );
    }

    public boolean useCoreMl() {
        return useCoreMl(null);
    }

    public boolean useCoreMl(Integer coreMlFlag) {
        if (coreMlFlag == null) {
            coreMlFlag = MaaInferenceDeviceEnum.AUTO.getValue();
        }
        return setInterfaceDevice(
                MaaInferenceExecutionProviderEnum.CORE_ML,
                coreMlFlag
        );
    }

    public boolean useAutoEp() {
        return setInterfaceDevice(
                MaaInferenceExecutionProviderEnum.AUTO,
                MaaInferenceDeviceEnum.AUTO
        );
    }

    /**
     * @deprecated please use `useDirectMl` instead.
     */
    public boolean setGpu(int gpuId) {
        if (gpuId < 0) return false;

        return this.useDirectMl(gpuId);
    }

    /**
     * @deprecated please use `useCpu` instead.
     */
    public boolean setCpu() {
        return this.useCpu();
    }

    /**
     * @deprecated please use `useAutoEp` instead.
     */
    public boolean setAutoDevice() {
        return this.useAutoEp();
    }

    private Integer status(long maaId) {
        return status(MaaResId.valueOf(maaId));
    }

    private Integer status(MaaResId maaId) {
        return MaaFramework.resource().MaaResourceStatus(this.handle, maaId).getValue();
    }

    private Integer waiting(long maaId) {
        return waiting(MaaResId.valueOf(maaId));
    }

    private Integer waiting(MaaResId maaId) {
        return MaaFramework.resource().MaaResourceWait(handle, maaId).getValue();
    }

    private Boolean setInterfaceDevice(MaaInferenceExecutionProviderEnum executionProviderEnum,
                                       MaaInferenceDeviceEnum interfaceDeviceEnum) {
        if (executionProviderEnum == null) return false;
        if (interfaceDeviceEnum == null) return false;
        return this.setInterfaceDevice(executionProviderEnum.getValue(), interfaceDeviceEnum.getValue());
    }

    private Boolean setInterfaceDevice(Integer executionProviderEnum,
                                       MaaInferenceDeviceEnum interfaceDeviceEnum) {
        if (executionProviderEnum == null) return false;
        if (interfaceDeviceEnum == null) return false;
        return this.setInterfaceDevice(executionProviderEnum, interfaceDeviceEnum.getValue());
    }

    private Boolean setInterfaceDevice(MaaInferenceExecutionProviderEnum executionProviderEnum,
                                       Integer interfaceDeviceEnum) {
        if (executionProviderEnum == null) return false;
        if (interfaceDeviceEnum == null) return false;
        return this.setInterfaceDevice(executionProviderEnum.getValue(), interfaceDeviceEnum);
    }

    private Boolean setInterfaceDevice(int executionProvider, int deviceId) {
        MaaOptionValue ep = MaaOptionValue.valueOf(executionProvider);
        MaaOptionValue device = MaaOptionValue.valueOf(deviceId);
        return MaaBool.TRUE.equals(
                MaaFramework.resource().MaaResourceSetOption(handle, MaaResOptionEnum.INFERENCE_DEVICE.value(),
                        device, device.getMaaOptionValueSize())
        ) && MaaBool.TRUE.equals(
                MaaFramework.resource().MaaResourceSetOption(handle, MaaResOptionEnum.INFERENCE_DEVICE.value(),
                        ep, ep.getMaaOptionValueSize())
        );
    }

}
