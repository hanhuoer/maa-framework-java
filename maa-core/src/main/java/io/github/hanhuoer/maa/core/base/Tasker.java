package io.github.hanhuoer.maa.core.base;

import com.alibaba.fastjson2.JSONObject;
import io.github.hanhuoer.maa.callbak.MaaNotificationCallback;
import io.github.hanhuoer.maa.consts.MaaGlobalOptionEnum;
import io.github.hanhuoer.maa.consts.MaaLoggingLevelEunm;
import io.github.hanhuoer.maa.consts.MaaRecognitionAlgorithmEnum;
import io.github.hanhuoer.maa.consts.MaaStatusEnum;
import io.github.hanhuoer.maa.core.util.TaskFuture;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.model.NodeDetail;
import io.github.hanhuoer.maa.model.RecognitionDetail;
import io.github.hanhuoer.maa.model.TaskDetail;
import io.github.hanhuoer.maa.ptr.StringBuffer;
import io.github.hanhuoer.maa.ptr.*;
import io.github.hanhuoer.maa.ptr.base.MaaBool;
import io.github.hanhuoer.maa.ptr.base.MaaLong;
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
    private MaaTaskerHandle handle;
    @Getter
    private Controller controller;
    @Getter
    private Resource resource;
    @Getter
    private boolean own;


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
            this.own = false;
        } else {
            this.handle = handle;
            this.own = true;
        }

        if (this.handle == null) throw new RuntimeException("Failed to create tasker.");
    }

    public static boolean setLogDir(String path) {
        MaaOptionValue maaOptionValue = new MaaOptionValue(path);

        MaaBool maaBool = MaaFramework.utility().MaaSetGlobalOption(
                MaaGlobalOptionEnum.LOG_DIR.value(), maaOptionValue, maaOptionValue.getMaaOptionValueSize()
        );
        return Boolean.TRUE.equals(
                maaBool.getValue()
        );
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

    public boolean bindResource(@NonNull Resource resource) {
        MaaBool maaBool = MaaFramework.tasker().MaaTaskerBindResource(this.handle, resource.getHandle());
        boolean equals = Boolean.TRUE.equals(maaBool.getValue());
        if (equals) {
            this.resource = resource;
        }
        return equals;
    }

    public boolean bindController(@NonNull Controller controller) {
        MaaBool maaBool = MaaFramework.tasker().MaaTaskerBindController(this.handle, controller.getHandle());
        boolean equals = Boolean.TRUE.equals(maaBool.getValue());
        if (equals) {
            this.controller = controller;
        }
        return equals;
    }

    public Resource resource() {
        MaaResourceHandle resourceHandle = MaaFramework.tasker().MaaTaskerGetResource(this.handle);
        if (resourceHandle == null)
            throw new RuntimeException("Failed to get resource.");

        return new Resource(resourceHandle);
    }

    public Controller controller() {
        MaaControllerHandle controllerHandle = MaaFramework.tasker().MaaTaskerGetController(this.handle);
        if (controllerHandle == null)
            throw new RuntimeException("Failed to get controller.");

        return new Controller(controllerHandle);
    }

    /**
     * check if the tasker is inited.
     *
     * @return true if the tasker is inited, false otherwise.
     */
    public boolean inited() {
        return Boolean.TRUE.equals(MaaFramework.tasker().MaaTaskerInited(this.handle).getValue());
    }

    public TaskFuture<TaskDetail> postPipeline(String entry) {
        return postPipeline(entry, "{}");
    }

    public TaskFuture<TaskDetail> postPipeline(String entry, Map<String, Object> pipelineOverride) {
        return postPipeline(entry, JSONObject.toJSONString(pipelineOverride));
    }

    public TaskFuture<TaskDetail> postPipeline(String entry, String pipelineOverride) {
        MaaTaskId maaTaskId = MaaFramework.tasker()
                .MaaTaskerPostPipeline(this.handle, entry, pipelineOverride);
        return this.genTaskJob(maaTaskId);
    }

//    public TaskFuture<TaskDetail> postRecognition(String entry) {
//        return postRecognition(entry, "{}");
//    }
//
//    public TaskFuture<TaskDetail> postRecognition(String entry, Map<String, Object> pipelineOverride) {
//        return postRecognition(entry, JSONObject.toJSONString(pipelineOverride));
//    }
//
//    public TaskFuture<TaskDetail> postRecognition(String entry, String pipelineOverride) {
//        MaaTaskId maaTaskId = MaaFramework.tasker()
//                .MaaTaskerPostRecognition(this.handle, entry, JSONObject.toJSONString(pipelineOverride));
//        return this.genTaskJob(maaTaskId);
//    }

//    public TaskFuture<TaskDetail> postAction(String entry) {
//        return postAction(entry, "{}");
//    }
//
//    public TaskFuture<TaskDetail> postAction(String entry, Map<String, Object> pipelineOverride) {
//        return postAction(entry, JSONObject.toJSONString(pipelineOverride));
//    }
//
//    public TaskFuture<TaskDetail> postAction(String entry, String pipelineOverride) {
//        MaaTaskId maaTaskId = MaaFramework.tasker()
//                .MaaTaskerPostAction(this.handle, entry, pipelineOverride);
//        return this.genTaskJob(maaTaskId);
//    }

    public NodeDetail getLatestNode(String name) {
        MaaLong.Reference latestId = new MaaLong.Reference();
        MaaBool maaBool = MaaFramework.tasker().MaaTaskerGetLatestNode(this.handle, name, latestId);
        if (!Boolean.TRUE.equals(maaBool.getValue())) {
            return null;
        }

        return getNodeDetail(latestId.getValue().longValue());
    }

    public boolean clearCache() {
        return Boolean.TRUE.equals(
                MaaFramework.tasker().MaaTaskerClearCache(this.handle).getValue()
        );
    }

    public TaskFuture<TaskDetail> genTaskJob(MaaTaskId maaTaskId) {
        return new TaskFuture<>(maaTaskId,
                id -> MaaStatusEnum.of(this.taskStatus(id.getValue())),
                id -> this.taskWait(id.getValue()),
                id -> this.getTaskDetail(maaTaskId));
    }

    public MaaStatus taskStatus(long id) {
        return MaaFramework.tasker().MaaTaskerStatus(this.handle, MaaTaskId.valueOf(id));
    }

    public MaaStatus taskWait(long id) {
        return MaaFramework.tasker().MaaTaskerWait(this.handle, MaaTaskId.valueOf(id));
    }

    private MaaStatusEnum stopStatus(long id) {
        if (this.running()) {
            return MaaStatusEnum.SUCCEEDED;
        } else {
            return MaaStatusEnum.RUNNING;
        }
    }

    private MaaStatusEnum stopWait(long id) {
        while (this.running()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return MaaStatusEnum.SUCCEEDED;
    }

    /**
     * check if running
     *
     * @return true if running else false.
     */
    public boolean running() {
        return Boolean.TRUE.equals(MaaFramework.tasker().MaaTaskerRunning(this.handle).getValue());
    }

    public RecognitionDetail getRecognitionDetail(long recoId) {
        return getRecognitionDetail(MaaRecoId.valueOf(recoId));
    }

    public RecognitionDetail getRecognitionDetail(MaaRecoId recoId) {
        StringBuffer name = new StringBuffer();
        StringBuffer algorithmHandle = new StringBuffer();
        MaaBool hit = new MaaBool();
        RectBuffer box = new RectBuffer();
        StringBuffer detailJson = new StringBuffer();
        ImageBuffer raw = new ImageBuffer();
        ImageListBuffer draws = new ImageListBuffer();

        MaaBool maaBool = MaaFramework.tasker().MaaTaskerGetRecognitionDetail(
                this.handle,
                recoId,
                name.getHandle(),
                algorithmHandle.getHandle(),
                hit,
                box.getHandle(),
                detailJson.getHandle(),
                raw.getHandle(),
                draws.getHandle()
        );

        boolean ret = Boolean.TRUE.equals(maaBool.getValue());
        if (!ret) return null;

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

        MaaRecognitionAlgorithmEnum algorithm = MaaRecognitionAlgorithmEnum.of(algorithmHandle.getValue());
        RecognitionDetail recognitionDetail = new RecognitionDetail()
                .setRecoId(recoId.getValue())
                .setName(name.getValue())
                .setAlgorithm(algorithm)
                .setHitBox(Boolean.TRUE.equals(hit.getValue()) ? box.getValue() : null)
                .setRawDetail(detailJson.getValue())
                .setRaw(rawImage)
                .setDraws(drawList);
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
//        MaaSize times = new MaaSize();
        MaaBool completed = new MaaBool();

        MaaBool maaBool = MaaFramework.tasker().MaaTaskerGetNodeDetail(
                this.handle,
                nodeId,
                name.getHandle(),
                recoId,
//                times,
                completed
        );

        if (!Boolean.TRUE.equals(maaBool.getValue())) return null;

        RecognitionDetail recognition = this.getRecognitionDetail(recoId.getValue().longValue());
        if (recognition == null) return null;

        NodeDetail detail = new NodeDetail()
                .setNodeId(nodeId.getValue())
                .setName(name.getValue())
                .setRecognition(recognition)
//                .setTimes(times.getValue())
                .setCompleted(completed.getValue());

        name.close();

        return detail;
    }

    public TaskDetail getTaskDetail(long taskId) {
        return getTaskDetail(MaaTaskId.valueOf(taskId));
    }

    public NodeDetail getTaskDetail(MaaNodeId maaNodeId) {
        return getNodeDetail(maaNodeId);
    }

    public RecognitionDetail getTaskDetail(MaaRecoId maaRecoId) {
        return getRecognitionDetail(maaRecoId);
    }

    public TaskDetail getTaskDetail(MaaTaskId taskId) {
        MaaSize.Reference size = new MaaSize.Reference();

        MaaBool maaBool = MaaFramework.tasker().MaaTaskerGetTaskDetail(
                this.handle, taskId, null, null, size
        );
        if (!Boolean.TRUE.equals(maaBool.getValue())) return null;

        StringBuffer entry = new StringBuffer();
        MaaNodeIdArr maaNodeIdArr = new MaaNodeIdArr(size.getValue().longValue());

        MaaBool ret = MaaFramework.tasker().MaaTaskerGetTaskDetail(
                this.handle,
                taskId,
                entry.getHandle(),
                maaNodeIdArr,
                size
        );
        if (!Boolean.TRUE.equals(ret.getValue())) return null;

        List<NodeDetail> nodeList = new ArrayList<>();
        for (int i = 0; i < size.getValue().longValue(); i++) {
            NodeDetail nodeDetail = this.getNodeDetail(maaNodeIdArr.getValue(i));
            nodeList.add(nodeDetail);
        }

        entry.close();

        return new TaskDetail()
                .setTaskId(taskId.getValue())
                .setEntry(entry.getValue())
                .setNodeDetails(nodeList)
                ;
    }

}
