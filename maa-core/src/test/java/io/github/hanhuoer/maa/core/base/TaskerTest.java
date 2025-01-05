package io.github.hanhuoer.maa.core.base;

import com.alibaba.fastjson2.JSONObject;
import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.consts.MaaLoggingLevelEunm;
import io.github.hanhuoer.maa.core.AdbController;
import io.github.hanhuoer.maa.core.custom.Context;
import io.github.hanhuoer.maa.core.custom.CustomAction;
import io.github.hanhuoer.maa.core.custom.CustomRecognition;
import io.github.hanhuoer.maa.core.util.TaskFuture;
import io.github.hanhuoer.maa.exception.ControllerNotBoundException;
import io.github.hanhuoer.maa.exception.ResourceNotBoundException;
import io.github.hanhuoer.maa.model.AdbInfo;
import io.github.hanhuoer.maa.model.RecognitionDetail;
import io.github.hanhuoer.maa.model.Rect;
import io.github.hanhuoer.maa.model.TaskDetail;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.awt.image.BufferedImage;
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
        Assertions.assertTrue(connect);
        Assertions.assertTrue(adbController.connected());

//        assert connect;

        tasker = new Tasker((message, detailsJson, callbackArg) -> {
            log.info("[tasker] received callback: ********************************************");
            log.info("[tasker] message: {}", message);
            log.info("[tasker] detailJson: {}", detailsJson);
            log.info("[tasker] callbackArg: {}", callbackArg);
        }, null);

        Assertions.assertTrue(Tasker.setLogDir("./resources/debug"));
        Assertions.assertTrue(Tasker.setRecording(true));
        Assertions.assertTrue(Tasker.setDebugMessage(true));
        Assertions.assertTrue(Tasker.setSaveDraw(true));
        Assertions.assertTrue(Tasker.setStdoutLevel(MaaLoggingLevelEunm.ALL));
//        Assertions.assertTrue(Tasker.setShowHitDraw(true));

        resource = new Resource((message, detailsJson, callbackArg) -> {
            log.info("[resource] received callback: ********************************************");
            log.info("[resource] message: {}", message);
            log.info("[resource] detailJson: {}", detailsJson);
            log.info("[resource] callbackArg: {}", callbackArg);
        }, null);

        resource.load("./resources/resource");
        Assertions.assertTrue(resource.loaded());
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

    @Test
    void controllerTest() {
        Assertions.assertThrows(ControllerNotBoundException.class, () -> tasker.getController());
        Assertions.assertThrows(ResourceNotBoundException.class, () -> tasker.getResource());

        bind();

        Assertions.assertDoesNotThrow(() -> {
            tasker.getController();
        });
        Assertions.assertDoesNotThrow(() -> {
            tasker.getResource();
        });

        Assertions.assertNotNull(tasker.getController());
        Assertions.assertNotNull(tasker.getResource());
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
    void registerRecognizer() {
        bind();

        CustomRecognition customRecognition = new CustomRecognition() {
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
                return new CustomRecognition.AnalyzeResult(
                        new Rect().setX(0).setY(0).setW(600).setH(600),
                        "detail...."
                );
            }
        };

        boolean result = resource.registerRecognition("Custom_Rec", customRecognition);
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
        boolean b = resource.registerRecognition("MyRec", myCustomRecognizer);
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

    /**
     * <pre>
     *   "Task6": {
     *     "recognition": "TemplateMatch",
     *     "template": "sk.png",
     *     "action": "Click"
     *   }
     * </pre>
     */
    @Test
    void templateMatchGpuTest() {
        bind();

        resource.setGpu(1);

        TaskFuture<TaskDetail> task = tasker.postPipeline("Task6");
        task.waiting();
        TaskDetail taskDetail = task.get();
        log.info("task detail: {}", taskDetail);

//        while (true) {
//            tasker.pipeline("Task6");
//        }
    }


    /**
     * <pre>
     *   "Task4": {
     *     "recognition": "OCR",
     *     "expected": "Sketchbook",
     *     "action": "Click"
     *   }
     * </pre>
     */
    @Test
    void OcrGpuTest() {
        bind();

        resource.setGpu(1);
        TaskFuture<TaskDetail> task = tasker.postPipeline("Task4");
        task.waiting();
        TaskDetail taskDetail = task.get();
        log.info("task detail: {}", taskDetail);

//        while (true) {
//            tasker.pipeline("Task4");
//        }
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

    @SneakyThrows
    @Test
    public void testRegister() {
        bind();

        MyCustomAction customAction = new MyCustomAction();
        MyCustomRecognizer customRecognizer = new MyCustomRecognizer();

        boolean customActionResult = resource.registerAction("MyAct2", customAction);
        Assertions.assertTrue(customActionResult);

        boolean customRecognizerResult = resource.registerRecognition("MyRec2", customRecognizer);
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

    public static class MyCustomRecognizer extends CustomRecognition {

        public MyCustomRecognizer() {
            super();
        }

        @Override
        public CustomRecognition.AnalyzeResult analyze(Context context, AnalyzeArg arg) {
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

            return new CustomRecognition.AnalyzeResult(
                    new Rect().setX(0).setY(0).setW(600).setH(600),
                    "detail...."
            );
        }
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

    @Test
    public void testCustomScreenshotAction() {
        bind();

        MyScreenshotAction customAction = new MyScreenshotAction();

        boolean customActionResult = resource.registerAction("screenshotAction", customAction);
        Assertions.assertTrue(customActionResult);

        String pipeline = "{\n" +
                "      \"screenshot\": {\n" +
                "        \"pre_delay\": 0,\n" +
                "        \"action\": \"Custom\",\n" +
                "        \"post_delay\": 0,\n" +
                "        \"custom_action\": \"screenshotAction\"\n" +
                "      }\n" +
                "    }";
        TaskFuture<TaskDetail> taskFuture = tasker.postPipeline("screenshot", pipeline);
        taskFuture.waiting();
        TaskDetail taskDetail = taskFuture.get();
        log.info("taskDetail: {}", taskDetail);
    }

    public static class MyScreenshotAction extends CustomAction {
        @Override
        public CustomAction.RunResult run(Context context, RunArg arg) {
            log.info("my screenshot action");

            Controller controller = context.tasker().controller();
            BufferedImage screencap = controller.screencap();

            /*
            https://github.com/MaaXYZ/MaaFramework/issues/509
            "rec_icon": {
                "recognition": "TemplateMatch",
                "template": ["google.png", "sketchbook.png", "via.png"],
                "threshold": [0.5, 0.5, 0.5],
                "pre_delay": 0,
                "post_delay": 0,
                "method": 5,
                "count": 4
              }
            * */
            RecognitionDetail recognitionDetail = context.runRecognition("rec_icon", screencap);
            log.info("reco detail: {}", recognitionDetail);

            return CustomAction.RunResult.success();
        }
    }
}