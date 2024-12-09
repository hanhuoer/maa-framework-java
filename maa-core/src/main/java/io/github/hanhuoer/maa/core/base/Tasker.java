package io.github.hanhuoer.maa.core.base;

import com.alibaba.fastjson2.JSONObject;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.NativeLongByReference;
import io.github.hanhuoer.maa.buffer.ImageBuffer;
import io.github.hanhuoer.maa.buffer.ImageListBuffer;
import io.github.hanhuoer.maa.buffer.RectBuffer;
import io.github.hanhuoer.maa.buffer.StringBuffer;
import io.github.hanhuoer.maa.callbak.MaaNotificationCallback;
import io.github.hanhuoer.maa.consts.MaaGlobalOptionEnum;
import io.github.hanhuoer.maa.consts.MaaLoggingLevelEunm;
import io.github.hanhuoer.maa.consts.MaaRecognitionAlgorithmEnum;
import io.github.hanhuoer.maa.consts.MaaStatusEnum;
import io.github.hanhuoer.maa.core.util.Future;
import io.github.hanhuoer.maa.core.util.Status;
import io.github.hanhuoer.maa.core.util.TaskFuture;
import io.github.hanhuoer.maa.define.*;
import io.github.hanhuoer.maa.define.base.MaaBool;
import io.github.hanhuoer.maa.define.base.MaaLong;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.model.NodeDetail;
import io.github.hanhuoer.maa.model.RecognitionDetail;
import io.github.hanhuoer.maa.model.TaskDetail;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author H
 */
@Slf4j
public class Tasker implements AutoCloseable {

    @Getter
    private final boolean own;
    @Getter
    private MaaTaskerHandle handle;
    private Controller controller;
    private Resource resource;
    private MaaNotificationCallback callback;
    private MaaCallbackTransparentArg callbackArg;


    public Tasker() {
        this(null);
    }

    public Tasker(MaaTaskerHandle handle) {
        this(handle, null, null);
    }

    public Tasker(MaaNotificationCallback callback, MaaCallbackTransparentArg callbackArg) {
        this(null, callback, callbackArg);
    }

    public Tasker(MaaTaskerHandle handle, MaaNotificationCallback callback, MaaCallbackTransparentArg callbackArg) {
        if (handle == null) {
            this.handle = MaaFramework.tasker().MaaTaskerCreate(callback, callbackArg);
            this.own = true;
        } else {
            this.handle = handle;
            this.own = false;
        }

        if (this.handle == null) throw new RuntimeException("Failed to create tasker.");

        this.callback = callback;
        this.callbackArg = callbackArg;
    }

    public static boolean setLogDir(String path) {
        MaaOptionValue maaOptionValue = new MaaOptionValue(path);

        MaaBool maaBool = MaaFramework.utility().MaaSetGlobalOption(
                MaaGlobalOptionEnum.LOG_DIR.value(), maaOptionValue, maaOptionValue.getMaaOptionValueSize()
        );
        return MaaBool.TRUE.equals(maaBool);
    }

    public static boolean setSaveDraw(boolean saveDraw) {
        MaaOptionValue maaOptionValue = new MaaOptionValue(saveDraw);

        return MaaFramework.utility().MaaSetGlobalOption(
                MaaGlobalOptionEnum.SAVE_DRAW.value(),
                maaOptionValue,
                maaOptionValue.getMaaOptionValueSize()
        ).getValue();
    }

    public static boolean setRecording(boolean recording) {
        MaaOptionValue maaOptionValue = new MaaOptionValue(recording);

        return MaaFramework.utility().MaaSetGlobalOption(
                MaaGlobalOptionEnum.RECORDING.value(),
                maaOptionValue,
                maaOptionValue.getMaaOptionValueSize()
        ).getValue();
    }

    public static boolean setStdoutLevel(MaaLoggingLevelEunm level) {
        MaaOptionValue maaOptionValue = new MaaOptionValue(level.getValue());

        return MaaFramework.utility().MaaSetGlobalOption(
                MaaGlobalOptionEnum.STDOUT_LEVEL.value(),
                maaOptionValue,
                maaOptionValue.getMaaOptionValueSize()
        ).getValue();
    }

    public static boolean setShowHitDraw(boolean showHitDraw) {
        MaaOptionValue maaOptionValue = new MaaOptionValue(showHitDraw);

        return MaaFramework.utility().MaaSetGlobalOption(
                MaaGlobalOptionEnum.SHOW_HIT_DRAW.value(),
                maaOptionValue,
                maaOptionValue.getMaaOptionValueSize()
        ).getValue();
    }

    public static boolean setDebugMessage(boolean debugMessage) {
        MaaOptionValue maaOptionValue = new MaaOptionValue(debugMessage);

        return MaaFramework.utility().MaaSetGlobalOption(
                MaaGlobalOptionEnum.DEBUG_MODE.value(),
                maaOptionValue,
                maaOptionValue.getMaaOptionValueSize()
        ).getValue();
    }

    @Override
    public void close() {
        if (handle == null) return;
        if (!own) return;
        MaaFramework.tasker().MaaTaskerDestroy(handle);
        this.handle = null;
        if (log.isDebugEnabled()) log.debug("tasker has bean destroyed.");
    }

    public boolean bind(Controller controller, Resource resource) {
        return this.bind(resource, controller);
    }

    /**
     * bind the resource and controller to the tasker.
     *
     * @param resource   the resource to bind.
     * @param controller the controller to bind.
     * @return true if the resource and controller were successfully bound, False otherwise.
     */
    public boolean bind(@NonNull Resource resource, @NonNull Controller controller) {
        return bindResource(resource) && bindController(controller);
    }

    public boolean bind(@NonNull Resource resource) {
        return bindResource(resource);
    }

    public boolean bind(@NonNull Controller controller) {
        return bindController(controller);
    }

    public boolean bindResource(@NonNull Resource resource) {
        MaaBool maaBool = MaaFramework.tasker().MaaTaskerBindResource(this.handle, resource.getHandle());
        boolean equals = MaaBool.TRUE.equals(maaBool);
        if (equals) {
            this.resource = resource;
        }
        return equals;
    }

    public boolean bindController(@NonNull Controller controller) {
        MaaBool maaBool = MaaFramework.tasker().MaaTaskerBindController(this.handle, controller.getHandle());
        boolean equals = MaaBool.TRUE.equals(maaBool);
        if (equals) {
            this.controller = controller;
        }
        return equals;
    }

    public Resource resource() {
        MaaResourceHandle resourceHandle = MaaFramework.tasker().MaaTaskerGetResource(this.handle);
        if (resourceHandle == null)
            throw new RuntimeException("Failed to get resource.");

        resource = new Resource(resourceHandle);
        return resource;
    }

    public Controller controller() {
        MaaControllerHandle controllerHandle = MaaFramework.tasker().MaaTaskerGetController(this.handle);
        if (controllerHandle == null)
            throw new RuntimeException("Failed to get controller.");

        controller = new Controller(controllerHandle);
        return controller;
    }

    /**
     * check if the tasker is inited.
     *
     * @return true if the tasker is inited, false otherwise.
     */
    public boolean inited() {
//        if (controller == null || resource == null) return false;
        MaaBool maaBool = MaaFramework.tasker().MaaTaskerInited(this.handle);
        return MaaBool.TRUE.equals(maaBool);
    }

    /**
     * check if running
     *
     * @return true if running else false.
     */
    public boolean running() {
        Boolean maaBool = MaaFramework.tasker().MaaTaskerRunning(this.handle);
        return MaaBool.TRUE.equals(maaBool);
    }

    public Future postStop() {
        MaaTaskId maaTaskId = MaaFramework.tasker().MaaTaskerPostStop(this.handle);
        return genTaskJob(maaTaskId);
    }

    public NodeDetail getLatestNode(String name) {
        MaaLong.Reference latestId = new MaaLong.Reference();
        MaaBool maaBool = MaaFramework.tasker().MaaTaskerGetLatestNode(this.handle, name, latestId);
        if (!MaaBool.TRUE.equals(maaBool)) {
            return null;
        }

        return getNodeDetail(latestId.getValue().longValue());
    }

    public boolean clearCache() {
        return MaaBool.TRUE.equals(
                MaaFramework.tasker().MaaTaskerClearCache(this.handle)
        );
    }

    /**
     * @since 2.0.0
     * @deprecated use pipeline instead.
     */
    @Deprecated
    public TaskDetail runTask(String entry) {
        return postPipeline(entry).waiting().get();
    }

    /**
     * @since 2.0.0
     * @deprecated use pipeline instead.
     */
    @Deprecated
    public TaskDetail runTask(String entry, Map<String, Object> pipelineOverride) {
        return postPipeline(entry, pipelineOverride).waiting().get();
    }

    /**
     * @since 2.0.0
     * @deprecated use pipeline instead.
     */
    @Deprecated
    public TaskDetail runTask(String entry, JSONObject pipelineOverride) {
        return postPipeline(entry, pipelineOverride).waiting().get();
    }

    /**
     * @since 2.0.0
     * @deprecated use pipeline instead.
     */
    @Deprecated
    public TaskDetail runTask(String entry, String pipelineOverride) {
        return postPipeline(entry, pipelineOverride).waiting().get();
    }

    public TaskDetail pipeline(String entry) {
        return postPipeline(entry).waiting().get();
    }

    public TaskDetail pipeline(String entry, Map<String, Object> pipelineOverride) {
        return postPipeline(entry, pipelineOverride).waiting().get();
    }

    public TaskDetail pipeline(String entry, JSONObject pipelineOverride) {
        return postPipeline(entry, pipelineOverride).waiting().get();
    }

    public TaskDetail pipeline(String entry, String pipelineOverride) {
        return postPipeline(entry, pipelineOverride).waiting().get();
    }

    public TaskFuture<TaskDetail> postPipeline(String entry) {
        return postPipeline(entry, "{}");
    }

    public TaskFuture<TaskDetail> postPipeline(String entry, Map<String, Object> pipelineOverride) {
        return postPipeline(entry, JSONObject.toJSONString(pipelineOverride));
    }

    public TaskFuture<TaskDetail> postPipeline(String entry, JSONObject pipelineOverride) {
        return postPipeline(entry, pipelineOverride.toString());
    }

    public TaskFuture<TaskDetail> postPipeline(String entry, String pipelineOverride) {
        MaaTaskId maaTaskId = MaaFramework.tasker()
                .MaaTaskerPostPipeline(this.handle, entry, pipelineOverride);
        return this.genTaskJob(maaTaskId);
    }

    public TaskFuture<TaskDetail> genTaskJob(MaaTaskId maaTaskId) {
        return new TaskFuture<>(maaTaskId,
                id -> MaaStatusEnum.of(this.taskStatus(id.getValue())),
                id -> {
                    this.taskWait(id.getValue());
                    return null;
                },
                id -> this.getTaskDetail(maaTaskId));
    }

    public MaaStatus taskStatus(long id) {
        return MaaFramework.tasker().MaaTaskerStatus(this.handle, MaaTaskId.valueOf(id));
    }

    public MaaStatus taskWait(long id) {
        return MaaFramework.tasker().MaaTaskerWait(this.handle, MaaTaskId.valueOf(id));
    }

    public RecognitionDetail getRecognitionDetail(long recoId) {
        return getRecognitionDetail(MaaRecoId.valueOf(recoId));
    }

    public RecognitionDetail getRecognitionDetail(MaaRecoId recoId) {
        StringBuffer name = new StringBuffer();
        StringBuffer algorithm = new StringBuffer();
        MaaBool hit = new MaaBool();
        RectBuffer box = new RectBuffer();
        StringBuffer detailJson = new StringBuffer();
        ImageBuffer raw = new ImageBuffer();
        ImageListBuffer draws = new ImageListBuffer();

        MaaBool maaBool = MaaFramework.tasker().MaaTaskerGetRecognitionDetail(
                this.handle,
                recoId,
                name.getHandle(),
                algorithm.getHandle(),
                hit,
                box.getHandle(),
                detailJson.getHandle(),
                raw.getHandle(),
                draws.getHandle()
        );

        if (!MaaBool.TRUE.equals(maaBool)) return null;

        BufferedImage rawImage = null;
        try {
            rawImage = raw.getValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<BufferedImage> drawList = null;
        try {
            drawList = draws.getValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MaaRecognitionAlgorithmEnum algorithmEnum = MaaRecognitionAlgorithmEnum.of(algorithm.getValue());
        RecognitionDetail recognitionDetail = new RecognitionDetail()
                .setRecoId(recoId.getValue())
                .setName(name.getValue())
                .setAlgorithm(algorithmEnum)
                .setHitBox(MaaBool.TRUE.equals(hit) ? box.getValue() : null)
                .setRawDetail(detailJson.getValue())
                .setRawImage(rawImage)
                .setDrawImages(drawList);
        recognitionDetail.parseRecognitionRawDetail();

        name.close();
        box.close();
        detailJson.close();
        raw.close();
        draws.close();

        return recognitionDetail;
    }

    public NodeDetail getNodeDetail(long nodeId) {
        return getNodeDetail(MaaNodeId.valueOf(nodeId));
    }

    public NodeDetail getNodeDetail(MaaNodeId nodeId) {
        StringBuffer name = new StringBuffer();
        MaaLong.Reference recoId = new MaaLong.Reference();
        MaaBool completed = new MaaBool();

        MaaBool maaBool = MaaFramework.tasker().MaaTaskerGetNodeDetail(
                this.handle,
                nodeId,
                name.getHandle(),
                recoId,
                completed
        );

        if (!MaaBool.TRUE.equals(maaBool))
            return null;

        RecognitionDetail recognition = this.getRecognitionDetail(recoId.getValue().longValue());
        if (recognition == null) return null;

        NodeDetail detail = new NodeDetail()
                .setNodeId(nodeId.getValue())
                .setName(name.getValue())
                .setRecognition(recognition)
                .setCompleted(completed.getValue());

        name.close();

        return detail;
    }

    public NodeDetail getTaskDetail(MaaNodeId maaNodeId) {
        return getNodeDetail(maaNodeId);
    }

    public RecognitionDetail getTaskDetail(MaaRecoId maaRecoId) {
        return getRecognitionDetail(maaRecoId);
    }

    public TaskDetail getTaskDetail(long taskId) {
        return getTaskDetail(MaaTaskId.valueOf(taskId));
    }

    public TaskDetail getTaskDetail(MaaTaskId taskId) {
        MaaSize.Reference size = new MaaSize.Reference();
        StringBuffer entry = new StringBuffer();

        MaaStatus.Reference statusReference = new MaaStatus.Reference();
        MaaBool maaBool = MaaFramework.tasker().MaaTaskerGetTaskDetail(
                this.handle, taskId, entry.getHandle(), null, size, statusReference
        );
        if (!MaaBool.TRUE.equals(maaBool))
            return null;
        if (size.getValue().intValue() == 0)
            return null;

        NativeLongByReference[] maaNodeIds = new NativeLongByReference[size.getValue().intValue()];
        MaaLong.Reference status = new MaaStatus.Reference();
        MaaBool ret = MaaFramework.tasker().MaaTaskerGetTaskDetail(
                this.handle,
                taskId,
                entry.getHandle(),
                maaNodeIds,
                size,
                status
        );
        if (!MaaBool.TRUE.equals(ret))
            return null;

        List<NodeDetail> nodeList = new ArrayList<>();
        for (int i = 0; i < size.getValue().longValue(); i++) {
            long maaNodeId = Pointer.nativeValue(maaNodeIds[i].getPointer());
            NodeDetail nodeDetail = this.getNodeDetail(maaNodeId);
            nodeList.add(nodeDetail);
        }

        String entryValue = entry.getValue();
        entry.close();

        return new TaskDetail()
                .setTaskId(taskId.getValue())
                .setEntry(entryValue)
                .setNodeDetails(nodeList)
                .setStatus(new Status(status))
                ;
    }

}
