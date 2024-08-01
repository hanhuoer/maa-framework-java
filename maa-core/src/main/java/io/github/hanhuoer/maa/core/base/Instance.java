package io.github.hanhuoer.maa.core.base;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import io.github.hanhuoer.maa.callbak.MaaInstanceCallback;
import io.github.hanhuoer.maa.consts.MaaGlobalOptionEnum;
import io.github.hanhuoer.maa.consts.MaaLoggingLevelEunm;
import io.github.hanhuoer.maa.consts.MaaStatusEnum;
import io.github.hanhuoer.maa.core.custom.CustomAction;
import io.github.hanhuoer.maa.core.custom.CustomRecognizer;
import io.github.hanhuoer.maa.core.util.TaskFuture;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.model.NodeDetail;
import io.github.hanhuoer.maa.model.RecognitionDetail;
import io.github.hanhuoer.maa.model.TaskDetail;
import io.github.hanhuoer.maa.ptr.StringBuffer;
import io.github.hanhuoer.maa.ptr.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author H
 */
@Slf4j
public class Instance implements AutoCloseable {

    private MaaInstanceHandle handle;
    protected ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public Instance() {
        this(null, null);
    }

    public Instance(MaaInstanceCallback callback, MaaCallbackTransparentArg callbackArg) {
        this.handle = MaaFramework.instance().MaaCreate(callback, callbackArg);
        if (this.handle == null) throw new RuntimeException("Failed to create resource.");
    }

    public static boolean setLogDir(String path) {
        MaaOptionValue maaOptionValue = new MaaOptionValue();
        Memory memory = new Memory(path.length() + 1);
        memory.setString(0, path);
        maaOptionValue.setPointer(memory);

        return Boolean.TRUE.equals(
                MaaFramework.utility().MaaSetGlobalOption(
                        MaaGlobalOptionEnum.LOG_DIR.getValue(), maaOptionValue, path.length()
                )
        );
    }

    public static boolean setSaveDraw(boolean saveDraw) {
        MaaOptionValue maaOptionValue = new MaaOptionValue();
        int nativeSize = Native.BOOL_SIZE;
        Memory memory = new Memory(nativeSize);
        memory.setByte(0, (byte) (saveDraw ? 1 : 0));
        maaOptionValue.setPointer(memory);

        return MaaFramework.utility().MaaSetGlobalOption(
                MaaGlobalOptionEnum.SAVE_DRAW.getValue(),
                maaOptionValue,
                nativeSize
        );
    }

    public static boolean setRecording(boolean recording) {
        MaaOptionValue maaOptionValue = new MaaOptionValue();
        int nativeSize = Native.BOOL_SIZE;
        Memory memory = new Memory(nativeSize);
        memory.setByte(0, (byte) (recording ? 1 : 0));
        maaOptionValue.setPointer(memory);

        return MaaFramework.utility().MaaSetGlobalOption(
                MaaGlobalOptionEnum.RECORDING.getValue(),
                maaOptionValue,
                nativeSize
        );
    }

    public static boolean setStdoutLevel(MaaLoggingLevelEunm level) {
        MaaOptionValue maaOptionValue = new MaaOptionValue();
        int nativeSize = Native.getNativeSize(int.class);
        Memory memory = new Memory(nativeSize);
        memory.setInt(0, level.getValue());
        maaOptionValue.setPointer(memory);

        return MaaFramework.utility().MaaSetGlobalOption(
                MaaGlobalOptionEnum.STDOUT_LEVEL.getValue(),
                maaOptionValue,
                nativeSize
        );
    }

    public static boolean setShowHitDraw(boolean showHitDraw) {
        MaaOptionValue maaOptionValue = new MaaOptionValue();
        int nativeSize = Native.BOOL_SIZE;
        Memory memory = new Memory(nativeSize);
        memory.setByte(0, (byte) (showHitDraw ? 1 : 0));
        maaOptionValue.setPointer(memory);

        return MaaFramework.utility().MaaSetGlobalOption(
                MaaGlobalOptionEnum.SHOW_HIT_DRAW.getValue(),
                maaOptionValue,
                nativeSize
        );
    }

    public static boolean setDebugMessage(boolean debugMessage) {
        MaaOptionValue maaOptionValue = new MaaOptionValue();
        int nativeSize = Native.BOOL_SIZE;
        Memory memory = new Memory(nativeSize);
        memory.setByte(0, (byte) (debugMessage ? 1 : 0));
        maaOptionValue.setPointer(memory);

        return MaaFramework.utility().MaaSetGlobalOption(
                MaaGlobalOptionEnum.DEBUG_MESSAGE.getValue(),
                maaOptionValue,
                nativeSize
        );
    }

    /**
     * query recognition detail.
     *
     * @param recognitionId the recognition id.
     * @return the recognition detail.
     */
    public static RecognitionDetail queryRecognitionDetail(long recognitionId) {
        MaaBool hit = new MaaBool();
        StringBuffer name = new StringBuffer();
        RectBuffer hitBox = new RectBuffer();
        StringBuffer detailJson = new StringBuffer();
        ImageBuffer raw = new ImageBuffer();
        ImageListBuffer draws = new ImageListBuffer();

        Boolean ret = MaaFramework.utility().MaaQueryRecognitionDetail(
                recognitionId,
                name.getHandle(),
                hit,
                hitBox.getHandle(),
                detailJson.getHandle(),
                raw.getHandle(),
                draws.getHandle()
        );
        if (!Boolean.TRUE.equals(ret)) return null;

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

        RecognitionDetail recognitionDetail = new RecognitionDetail()
                .setRecoId(recognitionId)
                .setName(name.getValue())
                .setHitBox(hit.getValue() ? hitBox.getValue() : null)
                .setDetail(detailJson.getValue())
                .setRaw(rawImage)
                .setDraws(drawList);

        name.close();
        hitBox.close();
        detailJson.close();
        raw.close();
        draws.close();

        return recognitionDetail;
    }

    /**
     * query running detail.
     *
     * @param nodeId the running id.
     * @return the running detail.
     */
    public static NodeDetail queryNodeDetail(long nodeId) {
        MaaRecoId recoId = new MaaRecoId();
        StringBuffer name = new StringBuffer();
        MaaBool runCompleted = new MaaBool();

        Boolean ret = MaaFramework.utility().MaaQueryNodeDetail(
                nodeId,
                name.getHandle(),
                recoId,
                runCompleted
        );
        if (!Boolean.TRUE.equals(ret)) return null;

        RecognitionDetail recognition = Instance.queryRecognitionDetail(recoId.getValue());
        if (recognition == null) return null;

        NodeDetail detail = new NodeDetail()
                .setNodeId(nodeId)
                .setName(name.getValue())
                .setRecognition(recognition)
                .setRunCompleted(runCompleted.getValue());

        name.close();

        return detail;
    }

    /**
     * query task detail.
     *
     * @param taskId the task id.
     * @return the task detail.
     */
    public static TaskDetail queryTaskDetail(long taskId) {
        MaaSize maaSize = new MaaSize();
        Boolean ret = MaaFramework.utility().MaaQueryTaskDetail(
                taskId, null, null, maaSize);
        if (!Boolean.TRUE.equals(ret)) return null;

        StringBuffer entry = new StringBuffer();
        MaaNodeIdArr maaNodeIdArr = new MaaNodeIdArr((int) maaSize.getValue());

        ret = MaaFramework.utility().MaaQueryTaskDetail(
                taskId, entry.getHandle(), maaNodeIdArr, maaSize);
        if (!Boolean.TRUE.equals(ret)) return null;

        List<NodeDetail> nodeDetails = new ArrayList<>();

        long size = maaSize.getValue();
        for (int i = 0; i < size; i++) {
            NodeDetail detail = Instance.queryNodeDetail(
                    maaNodeIdArr.getValue(i)
            );
            nodeDetails.add(detail);
        }

        TaskDetail taskDetail = new TaskDetail()
                .setTaskId(taskId)
                .setEntry(entry.getValue())
                .setNodeDetails(nodeDetails);

        entry.close();

        return taskDetail;
    }

    @Override
    public void close() {
        if (handle == null) return;
        MaaFramework.instance().MaaDestroy(handle);
        this.handle = null;
        if (log.isDebugEnabled()) log.debug("instance has bean destroyed.");
    }

    public boolean bind(Controller controller, Resource resource) {
        return this.bind(resource, controller);
    }

    /**
     * bind the resource and controller to the instance.
     *
     * @param resource   the resource to bind.
     * @param controller the controller to bind.
     * @return true if the resource and controller were successfully bound, False otherwise.
     */
    public boolean bind(@NonNull Resource resource, @NonNull Controller controller) {
        return bindResource(resource) && bindController(controller);
    }

    public boolean bindResource(@NonNull Resource resource) {
        return Boolean.TRUE.equals(MaaFramework.instance().MaaBindResource(this.handle, resource.getHandle()));
    }

    public boolean bindController(@NonNull Controller controller) {
        return Boolean.TRUE.equals(MaaFramework.instance().MaaBindController(this.handle, controller.getHandle()));
    }

    /**
     * check if the instance is inited.
     *
     * @return true if the instance is inited, false otherwise.
     */
    public boolean inited() {
        return Boolean.TRUE.equals(MaaFramework.instance().MaaInited(this.handle));
    }

    public TaskDetail runTask(String taskType) {
        return this.runTask(taskType, "{}");
    }

    /**
     * sync run a task.
     * <p>
     *
     * @param taskType: The name of the task.
     * @param param:    The param of the task.
     * @return details of the task.
     */
    public TaskDetail runTask(String taskType, String param) {
        TaskFuture taskFuture = this.postTask(taskType, param);
        taskFuture.waitForCompletion().join();
        return taskFuture.get();
    }

    public RecognitionDetail runRecognition(String taskType) {
        return runRecognition(taskType, "{}");
    }

    public RecognitionDetail runRecognition(String taskType, String param) {
        TaskFuture taskFuture = this.postRecognition(taskType, param);
        taskFuture.waitForCompletion().join();
        TaskDetail taskDetail = taskFuture.get();

        if (taskDetail == null) return null;
        if (taskDetail.getNodeDetails() == null) return null;
        if (taskDetail.getNodeDetails().isEmpty()) return null;

        return taskDetail.getNodeDetails().get(0).getRecognition();
    }

    public NodeDetail runAction(String taskType) {
        return runAction(taskType, "{}");
    }

    public NodeDetail runAction(String taskType, String param) {
        TaskFuture taskFuture = this.postAction(taskType, param);
        taskFuture.waitForCompletion().join();
        TaskDetail taskDetail = taskFuture.get();

        if (taskDetail == null) return null;
        if (taskDetail.getNodeDetails() == null) return null;
        if (taskDetail.getNodeDetails().isEmpty()) return null;

        return taskDetail.getNodeDetails().get(0);
    }

    public TaskFuture postTask(String taskType) {
        return postTask(taskType, "{}");
    }

    public TaskFuture postTask(String taskType, String param) {
        Long taskId = MaaFramework.instance().MaaPostTask(this.handle, taskType, param);

        Function<Long, MaaStatusEnum> statusFunc = id -> MaaStatusEnum.of(this.status(id));
        BiFunction<Long, String, Boolean> setParamFunc = this::setTaskParam;
        Function<Long, TaskDetail> queryDetailFunc = Instance::queryTaskDetail;

        return new TaskFuture(
                taskId,
                statusFunc,
                setParamFunc,
                queryDetailFunc
        );
    }

    public TaskFuture postRecognition(String taskType) {
        return postRecognition(taskType, "{}");
    }

    public TaskFuture postRecognition(String taskType, String param) {
        Long taskId = MaaFramework.instance().MaaPostRecognition(this.handle, taskType, param);

        Function<Long, MaaStatusEnum> statusFunc = maaid -> MaaStatusEnum.of(this.status(maaid));
        BiFunction<Long, String, Boolean> setParamFunc = this::setTaskParam;
        Function<Long, TaskDetail> queryDetailFunc = Instance::queryTaskDetail;

        return new TaskFuture(
                taskId,
                statusFunc,
                setParamFunc,
                queryDetailFunc
        );
    }

    public TaskFuture postAction(String taskType) {
        return postAction(taskType, "{}");
    }

    public TaskFuture postAction(String taskType, String param) {
        Long taskId = MaaFramework.instance().MaaPostAction(this.handle, taskType, param);

        Function<Long, MaaStatusEnum> statusFunc = maaid -> MaaStatusEnum.of(this.status(maaid));
        BiFunction<Long, String, Boolean> setParamFunc = this::setTaskParam;
        Function<Long, TaskDetail> queryDetailFunc = Instance::queryTaskDetail;

        return new TaskFuture(
                taskId,
                statusFunc,
                setParamFunc,
                queryDetailFunc
        );
    }

    /**
     * wait for all tasks to complete.
     */
    public void waitAll() {
        while (true) {
            if (!running()) return;
        }
    }

    /**
     * check if running
     *
     * @return true if running else false.
     */
    public boolean running() {
        return Boolean.TRUE.equals(MaaFramework.instance().MaaRunning(this.handle));
    }

    /**
     * sync stop all tasks.
     */
    public boolean stop() {
        return postStop().join();
    }

    /**
     * async stop all tasks.
     */
    public CompletableFuture<Boolean> postStop() {
        MaaFramework.instance().MaaPostStop(this.handle);
        return CompletableFuture.supplyAsync(() -> stopStatus() == MaaStatusEnum.SUCCESS.getValue(), executor);
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

        Boolean result = MaaFramework.instance().MaaRegisterCustomRecognizer(
                this.handle,
                name,
                recognizer.getHandle(),
                null
        );

        return Boolean.TRUE.equals(result);
    }

    public boolean registerAction(String name, CustomAction action) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Action name cannot be null or empty");
        }
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }

        Boolean result = MaaFramework.instance().MaaRegisterCustomAction(
                this.handle,
                name,
                action.getHandle(),
                null
        );

        return Boolean.TRUE.equals(result);
    }

    public Integer status(long id) {
        return MaaFramework.instance().MaaTaskStatus(this.handle, id);
    }

    public int stopStatus() {
        boolean running = running();
        if (running) {
            return MaaStatusEnum.RUNNING.getValue();
        } else {
            return MaaStatusEnum.SUCCESS.getValue();
        }
    }

    public boolean setTaskParam(long taskId, String param) {
        return Boolean.TRUE.equals(MaaFramework.instance().MaaSetTaskParam(handle, taskId, param));
    }

}
