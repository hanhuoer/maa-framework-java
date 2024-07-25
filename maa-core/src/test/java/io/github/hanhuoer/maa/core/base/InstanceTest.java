package io.github.hanhuoer.maa.core.base;

import com.alibaba.fastjson2.JSONObject;
import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.core.AdbController;
import io.github.hanhuoer.maa.core.base.Instance;
import io.github.hanhuoer.maa.core.base.Resource;
import io.github.hanhuoer.maa.core.custom.CustomAction;
import io.github.hanhuoer.maa.core.custom.CustomRecognizer;
import io.github.hanhuoer.maa.core.custom.SyncContext;
import io.github.hanhuoer.maa.core.util.TaskFuture;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.model.Rect;
import io.github.hanhuoer.maa.ptr.StringBuffer;
import io.github.hanhuoer.maa.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.awt.image.BufferedImage;
import java.util.List;

@Slf4j
class InstanceTest {

    private Instance instance;
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

        adbController = new AdbController(adbInfo, (msg, detailsJson, callbackArg) -> {
            log.info("[controller] received callback: ********************************************");
            log.info("[controller] message: {}", msg);
            log.info("[controller] detailJson: {}", detailsJson);
            log.info("[controller] callbackArg: {}", callbackArg);
        }, null);
        log.info("adb controller: {}", adbController);
        log.info("adb connected: {}", adbController.connected());

        log.info("adb controller start connecting");
        boolean connect = adbController.connect();

//        assert connect;

        instance = new Instance((msg, detailsJson, callbackArg) -> {
            log.info("[instance] received callback: ********************************************");
            log.info("[instance] message: {}", msg);
            log.info("[instance] detailJson: {}", detailsJson);
            log.info("[instance] callbackArg: {}", callbackArg);
        }, null);

//        Instance.setLogDir("./resources/debug");
//        Instance.setRecording(true);
//        Instance.setDebugMessage(true);
//        Instance.setSaveDraw(true);
//        Instance.setStdoutLevel(MaaLoggingLevelEunm.ALL);
//        Instance.setShowHitDraw(true);

        resource = new Resource((msg, detailsJson, callbackArg) -> {
            log.info("[resource] received callback: ********************************************");
            log.info("[resource] message: {}", msg);
            log.info("[resource] detailJson: {}", detailsJson);
            log.info("[resource] callbackArg: {}", callbackArg);
        }, null);

        resource.load("./resources/resource").join();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (instance != null)
            instance.close();
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
        boolean bind = instance.bind(resource, adbController);
        assert bind;
        log.info("bind result: {}", bind);
    }

    @Test
    void inited() {
        log.info("inited result: {}", instance.inited());

        bind();

        log.info("inited result: {}", instance.inited());
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
//        TaskDetail taskDetail = instance.runTask("Task6", "{}");
        TaskDetail taskDetail = instance.runTask("yolo_detect_click", "{}");
        log.info("run task result: {}", taskDetail);
    }

    @Test
    void runRecognition() {
        bind();
        RecognitionDetail detail = instance.runRecognition("检测并点击", "{}");
        log.info("run task Recognition Detail: {}", detail);
    }

    @Test
    void runAction() {
        bind();
        NodeDetail task5 = instance.runAction("Task5", "{}");
        log.info("run task action result: {}", task5);
    }

    @Test
    void postTask() {
        bind();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("recognition", "TemplateMatch");
        jsonObject.put("template", "sk.png");
        jsonObject.put("action", "Click");
        TaskFuture taskFuture = instance.postTask("Task6", "{}");
        taskFuture.waitForCompletion().join();
        TaskDetail taskDetail = taskFuture.get();
        log.info("task detail: {}", taskDetail);
    }

    @Test
    void postRecognition() {
        bind();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("recognition", "TemplateMatch");
        jsonObject.put("template", "sk.png");
        jsonObject.put("action", "Click");
        TaskFuture taskFuture = instance.postTask("Task6", "{}");
        taskFuture.waitForCompletion().join();
        TaskDetail taskDetail = taskFuture.get();
        log.info("task detail: {}", taskDetail);
    }

    @Test
    void postAction() {
    }

    @Test
    void waitAll() {
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
            public CustomRecognizerAnalyzeResult analyze(SyncContext context, BufferedImage image,
                                                         String taskName, String customParam) {
                log.info("[custom recognizer]");
                log.info("context: {}", context);
                log.info("image: {}", image);
                log.info("taskName: {}", taskName);
                log.info("customParam: {}", customParam);
                return new CustomRecognizerAnalyzeResult(
                        true,
                        new Rect().setX(0).setY(0).setW(600).setH(600),
                        "detail...."
                );
            }
        };

        boolean b = instance.registerRecognizer("Custom_Rec", customRecognizer);
        log.info("register recognizer: {}", b);
    }

    @Test
    void registerAction() {
        bind();

        CustomAction customAction = new CustomAction() {
            @Override
            public boolean run(SyncContext context, String taskName, String customParam, Rect box, String recDetail) {
                log.info("run.");
                log.info("context: {}", context);
                log.info("taskName: {}", taskName);
                log.info("customParam: {}", customParam);
                log.info("box: {}", box);
                log.info("recDetail: {}", recDetail);

                boolean swipe = context.swipe(100, 100, 450, 600, 1 * 1000);
                log.info("swipe: {}", swipe);

                return true;
            }

            @Override
            public void stop() {
                log.info("stop.");
            }
        };

        boolean b = instance.registerAction("MyAct", customAction);
        log.info("register action: {}", b);
    }

    @Test
    void registerRecognizerTest() throws Exception {
        bind();

        MyCustomRecognizer myCustomRecognizer = new MyCustomRecognizer();
        boolean b = instance.registerRecognizer("MyRec", myCustomRecognizer);
        log.info("register recognizer: {}", b);

        StringBuffer stringBuffer = new StringBuffer();
        Boolean taskListResult = MaaFramework.resource().MaaResourceGetTaskList(resource.getHandle(), stringBuffer.getHandle());
        log.info("task list result: {}", taskListResult);
        log.info("task list result: {}", stringBuffer.getValue());

        RecognitionDetail recognition = instance.runRecognition("MyRec", "{}");
        log.info("register recognition result: {}", recognition);
        TaskDetail taskDetail = instance.runTask("start", "{}");
        log.info("register recognition taskDetail: {}", taskDetail);
    }

    @Test
    void registerActionTest() throws Exception {
        bind();

        MyCustomAction myCustomAction = new MyCustomAction();
        boolean b = instance.registerAction("MyAct", myCustomAction);
        log.info("register action: {}", b);

        TaskDetail taskDetail = instance.runTask("act", "{}");
        log.info("action result: {}", taskDetail);
    }


    public static class MyCustomAction extends CustomAction {
        @Override
        public boolean run(SyncContext context, String taskName, String customParam, Rect box, String recDetail) {
            log.info("run.");
            log.info("context: {}", context);
            log.info("taskName: {}", taskName);
            log.info("customParam: {}", customParam);
            log.info("box: {}", box);
            log.info("recDetail: {}", recDetail);

            boolean swipe = context.swipe(100, 100, 600, 600, 1 * 1000);
            log.info("swipe: {}", swipe);

            return true;
        }

        @Override
        public void stop() {
            log.info("stop.");
        }
    }

    ;

    public static class MyCustomRecognizer extends CustomRecognizer {

        public MyCustomRecognizer() {
            super();
        }

        @Override
        public CustomRecognizerAnalyzeResult analyze(SyncContext context, BufferedImage image,
                                                     String taskName, String customParam) {
            log.info("[custom recognizer]");
            log.info("context: {}", context);
            log.info("image: {}", image);
            log.info("taskName: {}", taskName);
            log.info("customParam: {}", customParam);

            return new CustomRecognizerAnalyzeResult(
                    true,
                    new Rect().setX(0).setY(0).setW(600).setH(600),
                    "detail...."
            );
        }
    }

    ;
}