package io.github.hanhuoer.maa.core.custom;

import com.alibaba.fastjson2.JSONObject;
import io.github.hanhuoer.maa.buffer.ImageBuffer;
import io.github.hanhuoer.maa.buffer.RectBuffer;
import io.github.hanhuoer.maa.buffer.StringListBuffer;
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
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * @author H
 */
@Getter
@Slf4j
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

    public TaskDetail runTask(String entry) {
        return runTask(entry, "{}");
    }

    public TaskDetail runTask(String entry, Map<String, Object> pipelineOverride) {
        return runTask(entry, JSONObject.toJSONString(pipelineOverride));
    }

    public TaskDetail runTask(String entry, JSONObject pipelineOverride) {
        return runTask(entry, pipelineOverride.toString());
    }

    public TaskDetail runTask(String entry, String pipelineOverride) {
        return runPipeline(entry, pipelineOverride);
    }

    public TaskDetail runPipeline(String entry) {
        return runPipeline(entry, "{}");
    }

    public TaskDetail runPipeline(String entry, Map<String, Object> pipelineOverride) {
        return runPipeline(entry, JSONObject.toJSONString(pipelineOverride));
    }

    public TaskDetail runPipeline(String entry, JSONObject pipelineOverride) {
        return runPipeline(entry, pipelineOverride.toString());
    }

    public TaskDetail runPipeline(String entry, String pipelineOverride) {
        MaaTaskId maaTaskId = MaaFramework.context().MaaContextRunTask(this.handle, entry, pipelineOverride);
        if (maaTaskId == null) {
            return null;
        }

        return tasker.getTaskDetail(maaTaskId);
    }

    public RecognitionDetail runRecognition(String entry, BufferedImage image) {
        return runRecognition(entry, "{}", image);
    }

    public RecognitionDetail runRecognition(String entry, BufferedImage image, Map<String, Object> pipelineOverride) {
        return runRecognition(entry, pipelineOverride, image);
    }

    public RecognitionDetail runRecognition(String entry, Map<String, Object> pipelineOverride, BufferedImage image) {
        return runRecognition(entry, JSONObject.toJSONString(pipelineOverride), image);
    }

    public RecognitionDetail runRecognition(String entry, BufferedImage image, JSONObject pipelineOverride) {
        return runRecognition(entry, pipelineOverride, image);
    }

    public RecognitionDetail runRecognition(String entry, JSONObject pipelineOverride, BufferedImage image) {
        return runRecognition(entry, pipelineOverride.toString(), image);
    }

    public RecognitionDetail runRecognition(String entry, BufferedImage image, String pipelineOverride) {
        return runRecognition(entry, pipelineOverride, image);
    }

    public RecognitionDetail runRecognition(String entry, String pipelineOverride, BufferedImage image) {
        ImageBuffer imageBuffer = new ImageBuffer();
        imageBuffer.setValue(image);
        MaaRecoId maaRecoId = MaaFramework.context().MaaContextRunRecognition(this.handle,
                entry, pipelineOverride, imageBuffer.getHandle());
        if (maaRecoId == null) {
            return null;
        }

        return tasker.getRecognitionDetail(maaRecoId);
    }

    public NodeDetail runAction(String entry, Rect box, String recoDetail) {
        return runAction(entry, box, recoDetail, "{}");
    }

    public NodeDetail runAction(String entry, Rect box, String recoDetail, Map<String, Object> pipelineOverride) {
        return runAction(entry, box, recoDetail, JSONObject.toJSONString(pipelineOverride));
    }

    public NodeDetail runAction(String entry, Rect box, String recoDetail, JSONObject pipelineOverride) {
        return runAction(entry, box, recoDetail, pipelineOverride.toString());
    }

    public NodeDetail runAction(String entry, Rect box, String recoDetail, String pipelineOverride) {
        RectBuffer rectBuffer = new RectBuffer();
        rectBuffer.setValue(box);
        MaaNodeId maaNodeId = MaaFramework.context().MaaContextRunAction(this.handle,
                entry, pipelineOverride, rectBuffer.getHandle(), recoDetail);
        rectBuffer.close();

        if (maaNodeId == null) return null;

        return tasker.getTaskDetail(maaNodeId);
    }

    public boolean overridePipeline(Map<String, Object> pipelineOverride) {
        return overridePipeline(JSONObject.toJSONString(pipelineOverride));
    }

    public boolean overridePipeline(JSONObject pipelineOverride) {
        return overridePipeline(pipelineOverride.toString());
    }

    public boolean overridePipeline(String pipelineOverride) {
        return MaaBool.TRUE.equals(
                MaaFramework.context().MaaContextOverridePipeline(
                        this.handle, pipelineOverride
                )
        );
    }

    public boolean overrideNext(String nodeName, List<String> nextList) {
        try (StringListBuffer stringListBuffer = new StringListBuffer()) {
            stringListBuffer.setValue(nextList);
            return MaaBool.TRUE.equals(
                    MaaFramework.context().MaaContextOverrideNext(
                            this.handle, nodeName, stringListBuffer.getHandle()
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
            throw new RuntimeException("Task id is null.");

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