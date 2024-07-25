package io.github.hanhuoer.maa.core.base;

import io.github.hanhuoer.maa.callbak.MaaResourceCallback;
import io.github.hanhuoer.maa.consts.MaaStatusEnum;
import io.github.hanhuoer.maa.core.util.Future;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.ptr.MaaCallbackTransparentArg;
import io.github.hanhuoer.maa.ptr.MaaResourceHandle;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * @author H
 */
@Getter
@Slf4j
public class Resource implements AutoCloseable {

    private MaaResourceHandle handle;
    private final MaaResourceCallback callback;
    private final MaaCallbackTransparentArg callbackArg;
    protected ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    public Resource() {
        this(null, null);
    }

    public Resource(MaaResourceCallback callback, MaaCallbackTransparentArg callbackArg) {
        this.callback = callback;
        this.callbackArg = callbackArg;

        this.handle = MaaFramework.resource().MaaResourceCreate(callback, callbackArg);

        if (this.handle == null) {
            throw new RuntimeException("Failed to create resource.");
        }
    }

    @Override
    public void close() {
        if (this.handle == null) return;
        MaaFramework.resource().MaaResourceDestroy(this.handle);
        this.handle = null;
        if (log.isDebugEnabled()) log.debug("resource has bean destroyed.");
    }

    public CompletableFuture<Boolean> load(String path) {
        return this.postPath(path).waitForCompletion();
    }

    public Future postPath(String path) {
        Long maaId = MaaFramework.resource().MaaResourcePostPath(this.handle, path);
        Function<Long, MaaStatusEnum> statusFunc = id -> MaaStatusEnum.of(this.status(maaId));

        return new Future(maaId, statusFunc) {
        };
    }

    public boolean loaded() {
        return Boolean.TRUE.equals(
                MaaFramework.resource().MaaResourceLoaded(this.handle)
        );
    }

    public boolean clear() {
        return Boolean.TRUE.equals(
                MaaFramework.resource().MaaResourceClear(this.handle)
        );
    }

    public Integer status(long maaId) {
        return MaaFramework.resource().MaaResourceStatus(this.handle, maaId);
    }

}
