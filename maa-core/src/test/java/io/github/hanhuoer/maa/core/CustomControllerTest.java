package io.github.hanhuoer.maa.core;

import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.MaaOptions;
import io.github.hanhuoer.maa.core.util.TaskFuture;
import io.github.hanhuoer.maa.util.ImageUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.awt.image.BufferedImage;

@Slf4j
class CustomControllerTest {

    Maa maa;
    MyController controller;

    @BeforeAll
    static void init() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
        MaaOptions options = new MaaOptions();
        options.setKillAdbOnShutdown(false);
        maa = Maa.create(options);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (controller != null)
            controller.close();
    }

    @Test
    public void testCustom() {
        controller = new MyController();

        boolean result = controller.postConnection().waiting().succeeded();
        String uuid = controller.requestUuid();
        Assertions.assertEquals(uuid, "12345678");
        log.info("uuid: {}", uuid);

        result &= controller.postStartApp("custom_aaa").waiting().succeeded();
        result &= controller.postStopApp("custom_bbb").waiting().succeeded();
        TaskFuture<BufferedImage> imageTaskFuture = controller.postScreencap().waiting();
        result &= imageTaskFuture.succeeded();
        log.info("image: {}", ImageUtils.toBase64(imageTaskFuture.get()));

        result &= controller.postClick(100, 200).waiting().succeeded();
        result &= controller.postSwipe(100, 200, 300, 400, 200).waiting().succeeded();
        result &= controller.postTouchDown(1, 100, 100, 0).waiting().succeeded();
        result &= controller.postTouchMove(1, 200, 200, 0).waiting().succeeded();
        result &= controller.postTouchUp(1).waiting().succeeded();
        result &= controller.postPressKey(32).waiting().succeeded();
        result &= controller.postInputText("Hello World!").waiting().succeeded();

        Assertions.assertTrue(controller.getCount() >= 11);
        Assertions.assertTrue(result, "Filed to run custom controller.");
    }

    public static class MyController extends CustomController {

        @Getter
        private int count = 0;

        public MyController() {
            super();
        }

        private void increaseCount() {
            count++;
        }

        @Override
        public boolean connect() {
            log.info("on custom connect.");
            increaseCount();
            return true;
        }

        @Override
        public String requestUuid() {
            log.info("on request uuid.");
            increaseCount();
            return "12345678";
        }

        @Override
        public boolean startApp(String intent) {
            log.info("on custom start app, intent: {}", intent);
            increaseCount();
            return true;
        }

        @Override
        public boolean stopApp(String intent) {
            log.info("on custom stop app, intent: {}", intent);
            increaseCount();
            return true;
        }

        @Override
        public BufferedImage screencap() {
            log.info("on custom screencap");
            increaseCount();
            return new BufferedImage(1080, 1920, 3);
        }

        @Override
        public boolean click(int x, int y) {
            log.info("on custom click, x: {}, y: {}", x, y);
            increaseCount();
            return true;
        }

        @Override
        public boolean swipe(int x1, int y1, int x2, int y2, int duration) {
            log.info("on custom swipe, x1: {}, y1: {}, x2: {}, y2: {}, duration: {}",
                    x1, y1, x2, y2, duration);
            increaseCount();
            return true;
        }

        @Override
        public boolean touchDown(int contact, int x, int y, int pressure) {
            log.info("on custom touch down, contact: {}, x: {}, y: {}, pressure: {}",
                    contact, x, y, pressure);
            increaseCount();
            return true;
        }

        @Override
        public boolean touchMove(int contact, int x, int y, int pressure) {
            log.info("on custom touch move, contact: {}, x: {}, y: {}, pressure: {}",
                    contact, x, y, pressure);
            increaseCount();
            return true;
        }

        @Override
        public boolean touchUp(int contact) {
            log.info("on custom touch up, contact: {}", contact);
            increaseCount();
            return true;
        }

        @Override
        public boolean pressKey(int keycode) {
            log.info("on custom press key: {}", keycode);
            increaseCount();
            return true;
        }

        @Override
        public boolean inputText(String text) {
            log.info("on custom input text: {}", text);
            increaseCount();
            return true;
        }

    }


}