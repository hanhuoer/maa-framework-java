package io.github.hanhuoer.maa.core.base;

import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.consts.MaaLoggingLevelEunm;
import io.github.hanhuoer.maa.core.AdbController;
import io.github.hanhuoer.maa.model.AdbInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.List;

@Slf4j
public class ControllerTest {

    private Tasker tasker;
    private AdbController controller;
    private Resource resource;
    private Maa maa;


    @BeforeAll
    static void init() throws Exception {

    }

    @BeforeEach
    void setUp() {
        maa = Maa.create();

        List<AdbInfo> adbInfoList = AdbController.find();
        AdbInfo adbInfo = adbInfoList.get(0);

        controller = new AdbController(adbInfo, (message, detailsJson, callbackArg) -> {
            log.info("[controller] received callback: ********************************************");
            log.info("[controller] message: {}", message);
            log.info("[controller] detailJson: {}", detailsJson);
            log.info("[controller] callbackArg: {}", callbackArg);
        }, null);
        log.info("adb controller: {}", controller);
        log.info("adb connected: {}", controller.connected());

        log.info("adb controller start connecting");
        boolean connect = controller.connect();
        Assertions.assertTrue(connect);
        Assertions.assertTrue(controller.connected());

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
        if (resource != null) {
            resource.close();
        }
    }

    @Test
    public void setScreenshotTargetShortSideTest() {
        controller.setScreenshotTargetShortSide(666);
    }

    @Test
    public void setScreenshotUseRawSizeTest() {
        Assertions.assertTrue(controller.setScreenshotUseRawSize(true));
        Assertions.assertTrue(controller.setScreenshotUseRawSize(false));
    }

}
