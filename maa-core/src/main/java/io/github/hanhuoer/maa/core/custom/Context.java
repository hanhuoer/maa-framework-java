package io.github.hanhuoer.maa.core.custom;

import com.alibaba.fastjson2.JSONObject;
import io.github.hanhuoer.maa.core.base.Tasker;
import io.github.hanhuoer.maa.core.util.TaskFuture;
import io.github.hanhuoer.maa.define.*;
import io.github.hanhuoer.maa.define.base.MaaBool;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.model.NodeDetail;
import io.github.hanhuoer.maa.model.RecognitionDetail;
import io.github.hanhuoer.maa.model.Rect;
import io.github.hanhuoer.maa.model.TaskDetail;
import lombok.Getter;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * @author H
 */
@Getter
public class Context {

    private final MaaContextHandle handle;
    private final Tasker tasker;

    public Context(MaaContextHandle handle) {
        if (handle == null) {
            throw new RuntimeException("Context handle cannot be null");
        }
        this.handle = handle;

        MaaTaskerHandle taskerHandle = MaaFramework.context().MaaContextGetTasker(handle);
        if (taskerHandle == null) {
            throw new RuntimeException("Tasker handle cannot be null");
        }
        this.tasker = new Tasker(taskerHandle);
    }

    public TaskDetail runPipeline(String entry, Map<String, Object> pipelineOverride) {
        MaaTaskId maaTaskId = MaaFramework.context().MaaContextRunPipeline(this.handle, entry, JSONObject.toJSONString(pipelineOverride));
        if (maaTaskId == null) {
            return null;
        }

        return tasker.getTaskDetail(maaTaskId);
    }

    public RecognitionDetail runRecognition(String entry, Map<String, Object> pipelineOverride, BufferedImage image) {
        MaaRecoId maaRecoId;
        try (ImageBuffer imageBuffer = new ImageBuffer()) {
            imageBuffer.setValue(image);
            maaRecoId = MaaFramework.context().MaaContextRunRecognition(this.handle,
                    entry, JSONObject.toJSONString(pipelineOverride), imageBuffer.getHandle());
        }
        if (maaRecoId == null) {
            return null;
        }

        return tasker.getTaskDetail(maaRecoId);
    }

    public NodeDetail runAction(String entry, List<Integer> box, String recoDetail, Map<String, Object> pipelineOverride) {
        return runAction(entry, new Rect(box), recoDetail, pipelineOverride);
    }

    public NodeDetail runAction(String entry, int[] box, String recoDetail, Map<String, Object> pipelineOverride) {
        return runAction(entry, new Rect(box), recoDetail, pipelineOverride);
    }

    public NodeDetail runAction(String entry, Rect box, String recoDetail, Map<String, Object> pipelineOverride) {
        MaaNodeId maaNodeId;
        try (RectBuffer rectBuffer = new RectBuffer()) {
            rectBuffer.setValue(box);
            maaNodeId = MaaFramework.context().MaaContextRunAction(this.handle,
                    entry, JSONObject.toJSONString(pipelineOverride), rectBuffer.getHandle(), recoDetail);
        }
        if (maaNodeId == null) {
            return null;
        }

        return tasker.getTaskDetail(maaNodeId);
    }

    public boolean overridePipeline(Map<String, Object> pipelineOverride) {
        return MaaBool.TRUE.equals(
                MaaFramework.context().MaaContextOverridePipeline(
                        this.handle, JSONObject.toJSONString(pipelineOverride)
                )
        );
    }

    public boolean overrideNext(String name, List<String> nextList) {
        try (StringListBuffer stringListBuffer = new StringListBuffer()) {
            stringListBuffer.setValue(nextList);
            return MaaBool.TRUE.equals(
                    MaaFramework.context().MaaContextOverrideNext(
                            this.handle, name, stringListBuffer.getHandle()
                    )
            );
        }
    }

    public Tasker tasker() {
        return this.tasker;
    }

    public TaskFuture<TaskDetail> getTaskJob() {
        MaaTaskId maaTaskId = MaaFramework.context().MaaContextGetTaskId(this.handle);
        if (maaTaskId == null)
            throw new RuntimeException("Task id is Null.");

        return this.tasker.genTaskJob(maaTaskId);
    }

    /**
     * Creates a copy of the current Context instance.
     *
     * @return a new Context instance that is a copy of the current one.
     * @throws RuntimeException if the cloning process fails
     */
    public Context copy() {
        MaaContextHandle contextHandle = MaaFramework.context().MaaContextClone(this.handle);

        if (contextHandle == null) {
            throw new RuntimeException("Failed to clone.");
        }

        return new Context(contextHandle);
    }

}