package io.github.hanhuoer.maa.core.base;

import io.github.hanhuoer.maa.callbak.MaaNotificationCallback;
import io.github.hanhuoer.maa.consts.MaaStatusEnum;
import io.github.hanhuoer.maa.core.custom.CustomAction;
import io.github.hanhuoer.maa.core.custom.CustomRecognizer;
import io.github.hanhuoer.maa.core.util.Future;
import io.github.hanhuoer.maa.define.StringBuffer;
import io.github.hanhuoer.maa.define.*;
import io.github.hanhuoer.maa.define.base.MaaBool;
import io.github.hanhuoer.maa.jna.MaaFramework;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author H
 */
@Slf4j
public class Resource implements AutoCloseable {

    @Getter
    private MaaResourceHandle handle;
    @Getter
    private final MaaNotificationCallback callback;
    @Getter
    private final MaaCallbackTransparentArg callbackArg;
    @Getter
    private final boolean own;
    private final Map<String, CustomRecognizer> customRecognizerMap;
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

        this.customRecognizerMap = new HashMap<>();
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
        Consumer<MaaId> waitFunc = id -> this.waiting(id.getValue());

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

    /**
     * register a custom recognizer.
     *
     * @param name       the name of the custom recognizer.
     * @param recognizer the custom recognizer.
     */
    public boolean registerRecognizer(String name, CustomRecognizer recognizer) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Recognizer name cannot be null or empty");
        }
        if (recognizer == null) {
            throw new IllegalArgumentException("Recognizer cannot be null");
        }

        MaaBool maaBool = MaaFramework.resource().MaaResourceRegisterCustomRecognition(
                this.handle,
                name,
                recognizer,
                null
        );

        boolean equals = MaaBool.TRUE.equals(maaBool);
        if (equals) {
            customRecognizerMap.put(name, recognizer);
        }
        return equals;
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

    public boolean unregisterRecognizer(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Recognizer name cannot be null or empty");
        }

        MaaBool result = MaaFramework.resource().MaaResourceUnregisterCustomRecognition(
                this.handle,
                name
        );

        boolean equals = MaaBool.TRUE.equals(result);
        if (equals) {
            customRecognizerMap.remove(name);
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

    public boolean clearCustomRecognizer() {
        MaaBool result = MaaFramework.resource().MaaResourceClearCustomRecognition(this.handle);

        boolean equals = MaaBool.TRUE.equals(result);
        if (equals) {
            customRecognizerMap.clear();
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

    private Integer status(long maaId) {
        return MaaFramework.resource().MaaResourceStatus(this.handle, MaaResId.valueOf(maaId)).getValue();
    }

    private Integer waiting(long maaId) {
        return MaaFramework.resource().MaaResourceWait(handle, MaaResId.valueOf(maaId)).getValue();
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

}
