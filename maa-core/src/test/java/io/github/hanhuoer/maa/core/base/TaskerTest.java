package io.github.hanhuoer.maa.core.base;

import com.alibaba.fastjson2.JSONObject;
import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.consts.MaaLoggingLevelEunm;
import io.github.hanhuoer.maa.core.AdbController;
import io.github.hanhuoer.maa.core.custom.Context;
import io.github.hanhuoer.maa.core.custom.CustomAction;
import io.github.hanhuoer.maa.core.custom.CustomRecognizer;
import io.github.hanhuoer.maa.core.util.TaskFuture;
import io.github.hanhuoer.maa.model.AdbInfo;
import io.github.hanhuoer.maa.model.Rect;
import io.github.hanhuoer.maa.model.TaskDetail;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

@Slf4j
class TaskerTest {

    private Tasker tasker;
    private AdbController adbController;
    private Resource resource;
    private Maa maa;


    @BeforeAll
    static void init() throws Exception {
//        LibraryLoader loader = new MaaWindowsX8664LibraryLoader();
//        loader.loadLibrary();
    }

    @AfterAll
    static void after() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
        maa = Maa.create();

        List<AdbInfo> adbInfoList = AdbController.find();
        AdbInfo adbInfo = adbInfoList.get(0);

        adbController = new AdbController(adbInfo, (message, detailsJson, callbackArg) -> {
            log.info("[controller] received callback: ********************************************");
            log.info("[controller] message: {}", message);
            log.info("[controller] detailJson: {}", detailsJson);
            log.info("[controller] callbackArg: {}", callbackArg);
        }, null);
        log.info("adb controller: {}", adbController);
        log.info("adb connected: {}", adbController.connected());

        log.info("adb controller start connecting");
        boolean connect = adbController.connect();

//        assert connect;

        tasker = new Tasker((message, detailsJson, callbackArg) -> {
            log.info("[tasker] received callback: ********************************************");
            log.info("[tasker] message: {}", message);
            log.info("[tasker] detailJson: {}", detailsJson);
            log.info("[tasker] callbackArg: {}", callbackArg);
        }, null);

        Tasker.setLogDir("./resources/debug");
        Tasker.setRecording(true);
        Tasker.setDebugMessage(true);
        Tasker.setSaveDraw(true);
        Tasker.setStdoutLevel(MaaLoggingLevelEunm.ALL);
//        Tasker.setShowHitDraw(true);

        resource = new Resource((message, detailsJson, callbackArg) -> {
            log.info("[resource] received callback: ********************************************");
            log.info("[resource] message: {}", message);
            log.info("[resource] detailJson: {}", detailsJson);
            log.info("[resource] callbackArg: {}", callbackArg);
        }, null);

        resource.load("./resources/resource");
    }

    @AfterEach
    void tearDown() throws Exception {
        if (tasker != null)
            tasker.close();
        if (resource != null)
            resource.close();
        if (adbController != null)
            adbController.close();
    }

    @Test
    void close() {
    }

    @Test
    void bind() {
        boolean bind = tasker.bind(resource, adbController);
        Assertions.assertTrue(bind);
    }

    @Test
    void inited() {
        log.info("inited result: {}", tasker.inited());

        bind();

        log.info("inited result: {}", tasker.inited());
    }

    /**
     * <pre>
     * {
     *   "Task1": {
     *     "action": "StartApp",
     *     "package": "com.hortor.games.xyzw/com.hortorgames.gamesdk.common.LaunchActivity"
     *   },
     *   "Task2": {
     *     "action": "Click",
     *     "target": [
     *       450,
     *       600,
     *       10,
     *       10
     *     ]
     *   },
     *   "Task3": {
     *     "action": "Swipe",
     *     "begin": [
     *       185,
     *       1100,
     *       10,
     *       10
     *     ],
     *     "end": [
     *       525,
     *       1100,
     *       10,
     *       10
     *     ]
     *   },
     *   "Task4": {
     *     "recognition": "OCR",
     *     "expected": "Sketchbook",
     *     "action": "Click"
     *   },
     *   "Task5": {
     *     "recognition": "ColorMatch",
     *     "upper": [230, 106, 56],
     *     "lower": [230, 106, 56],
     *     "action": "Click"
     *   }
     * }
     * </pre>
     */
    @Test
    void runTask() {
        bind();
        log.info("starting...");
//        TaskFuture<TaskDetail> task = tasker.postPipeline("Task6", "{}");
        TaskFuture<TaskDetail> task = tasker.postPipeline("yolo_detect_click", "{}");
        log.info("waiting...");
        task.waiting();
        TaskDetail taskDetail = task.get();
        log.info("run task result: {}", taskDetail);
        log.info("...");
    }

    @Test
    void runTask2() {
        bind();
        log.info("starting...");
        TaskDetail taskDetail = tasker.pipeline("Task6", "{}");
//        TaskDetail taskDetail = tasker.pipeline("yolo_detect_click", "{}");
        log.info("run task result: {}", taskDetail);
        log.info("...");
    }

    @Test
    void postTask() {
        bind();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("recognition", "TemplateMatch");
        jsonObject.put("template", "sk.png");
        jsonObject.put("action", "Click");
        TaskFuture<TaskDetail> taskFuture = tasker.postPipeline("Task6", jsonObject);
        taskFuture.waiting();
        TaskDetail taskDetail = taskFuture.get();
        log.info("task detail: {}", taskDetail);
    }

    @Test
    void postAction() {
    }

    @Test
    void running() {
    }

    @Test
    void stop() {
    }

    @Test
    void postStop() {
    }

    @Test
    void registerRecognizer() {
        bind();

        CustomRecognizer customRecognizer = new CustomRecognizer() {
            @Override
            public AnalyzeResult analyze(Context context, AnalyzeArg arg) {
                log.info("[custom recognizer]");
                log.info("context: {}", context);
                log.info("taskDetail: {}", arg.getTaskDetail());
                log.info("currentTaskName: {}", arg.getCurrentTaskName());
                log.info("customRecognitionName: {}", arg.getCustomRecognitionName());
                log.info("customRecognitionParam: {}", arg.getCustomRecognitionParam());
                log.info("image: {}", arg.getImage());
                log.info("roi: {}", arg.getRoi());
                return new CustomRecognizer.AnalyzeResult(
                        new Rect().setX(0).setY(0).setW(600).setH(600),
                        "detail...."
                );
            }
        };

        boolean result = resource.registerRecognizer("Custom_Rec", customRecognizer);
        log.info("register recognizer result: {}", result);
        Assertions.assertTrue(result);
    }

    @Test
    void registerAction() {
        bind();

        CustomAction customAction = new CustomAction() {
            @Override
            public RunResult run(Context context, RunArg arg) {
                log.info("run.");
                log.info("context: {}", context);
                log.info("taskDetail: {}", arg.getTaskDetail());
                log.info("currentTaskName: {}", arg.getCurrentTaskName());
                log.info("customActionName: {}", arg.getCustomActionName());
                log.info("customActionParam: {}", arg.getCustomActionParam());
                log.info("recognitionDetail: {}", arg.getRecognitionDetail());
                log.info("box: {}", arg.getBox());

                boolean swipe = context.tasker().controller().swipe(100, 100, 450, 600, 1 * 1000);
                log.info("swipe: {}", swipe);

                return new RunResult().setSuccess(true);
            }

        };

        boolean result = resource.registerAction("MyAct", customAction);
        log.info("register action result: {}", result);
        Assertions.assertTrue(result);
    }

    @Test
    void registerRecognizerTest() throws Exception {
        bind();

        // register.
        MyCustomRecognizer myCustomRecognizer = new MyCustomRecognizer();
        boolean b = resource.registerRecognizer("MyRec", myCustomRecognizer);
        log.info("register recognizer: {}", b);

        // get the resource task list.
//        StringListBuffer stringBuffer = new StringListBuffer();
//        Boolean taskListResult = MaaFramework.resource()
//                .MaaResourceGetTaskList(resource.getHandle(), stringBuffer.getHandle())
//                .getValue();
//        List<String> taskList = stringBuffer.getValue();
//        stringBuffer.close();
//        log.info("get task list result: {}", taskListResult);
//        log.info("task list result: {}", taskList);

        // post recognition task.
//        JSONObject param = new JSONObject();
//        param.put("recognition", "Custom");
//        param.put("custom_recognition", "MyRec");
//        param.put("custom_recognition_param", new JSONObject());
//        TaskFuture<TaskDetail> taskFuture = tasker.postPipeline("Task3", param);
//        taskFuture.waiting();
//        TaskDetail recognition = taskFuture.get();
//        log.info("register recognition task result: {}", recognition);
        log.info("starting...");
//        TaskDetail taskDetail = tasker.postPipeline("start", "{}").waiting().get();
//        log.info("taskDetail: {}", taskDetail);
        tasker.postPipeline("start", "{}").waiting();
    }

    @Test
    void registerActionTest() throws Exception {
        bind();

        MyCustomAction myCustomAction = new MyCustomAction();
        boolean b = resource.registerAction("MyAct", myCustomAction);
        log.info("register action result: {}", b);

        TaskDetail taskDetail = tasker.postPipeline("act", "{}")
                .waiting().get();
        log.info("action task result: {}", taskDetail);
    }


    public static class MyCustomAction extends CustomAction {
        @Override
        public CustomAction.RunResult run(Context context, RunArg arg) {
            log.info("run.");
            log.info("context: {}", context);
            log.info("taskDetail: {}", arg.getTaskDetail());
            log.info("currentTaskName: {}", arg.getCurrentTaskName());
            log.info("customActionName: {}", arg.getCustomActionName());
            log.info("customActionParam: {}", arg.getCustomActionParam());
            log.info("recognitionDetail: {}", arg.getRecognitionDetail());
            log.info("box: {}", arg.getBox());

            boolean swipe = context.tasker().controller().swipe(100, 100, 600, 600, 1 * 1000);
            log.info("swipe: {}", swipe);

            return new CustomAction.RunResult().setSuccess(true);
        }

    }

    public static class MyCustomRecognizer extends CustomRecognizer {

        public MyCustomRecognizer() {
            super();
        }

        @Override
        public CustomRecognizer.AnalyzeResult analyze(Context context, AnalyzeArg arg) {
            log.info("[custom recognizer]");
            log.info("context: {}", context);
            log.info("taskDetail: {}", arg.getTaskDetail());
            log.info("currentTaskName: {}", arg.getCurrentTaskName());
            log.info("customRecognitionName: {}", arg.getCustomRecognitionName());
            log.info("customRecognitionParam: {}", arg.getCustomRecognitionParam());
            log.info("image: {}", arg.getImage());
            log.info("roi: {}", arg.getRoi());

            // make a new context to override the pipeline, only for itself
            Context newContext = context.copy();
            log.info("context: {}", newContext);

            context.overrideNext(arg.getCurrentTaskName(), List.of("TaskA", "TaskB"));

            return new CustomRecognizer.AnalyzeResult(
                    new Rect().setX(0).setY(0).setW(600).setH(600),
                    "detail...."
            );
        }
    }

    @SneakyThrows
    @Test
    public void test_register() {
        bind();

        MyCustomAction customAction = new MyCustomAction();
        MyCustomRecognizer customRecognizer = new MyCustomRecognizer();

        boolean customActionResult = resource.registerAction("MyAct2", customAction);
        Assertions.assertTrue(customActionResult);

        boolean customRecognizerResult = resource.registerRecognizer("MyRec2", customRecognizer);
        Assertions.assertTrue(customRecognizerResult);

        Assertions.assertTrue(tasker.inited());

        JSONObject customPipeline = getCustomPipeline();
        TaskFuture<TaskDetail> customTaskFuture = tasker.postPipeline("custom_task_test", customPipeline);
        customTaskFuture.waiting();
        Assertions.assertTrue(customTaskFuture.done());

        boolean customActionUnregister = resource.unregisterAction("MyAct2");
        Assertions.assertTrue(customActionUnregister);

        boolean customRecognizerUnregister = resource.unregisterRecognizer("MyRec2");
        Assertions.assertTrue(customRecognizerUnregister);

    }


    private JSONObject getCustomPipeline() {
        JSONObject pipelineJson = new JSONObject();
        JSONObject taskJson = new JSONObject();

        taskJson.put("recognition", "Custom");
        taskJson.put("custom_recognition", "MyRec2");
        taskJson.put("custom_recognition_param", new JSONObject(Map.of("param", "hi recognition")));

        taskJson.put("action", "Custom");
        taskJson.put("custom_action", "MyAct2");
        taskJson.put("custom_action_param", new JSONObject(Map.of("param", "hi action")));

        pipelineJson.put("custom_task_test", taskJson);
        return pipelineJson;
    }
}