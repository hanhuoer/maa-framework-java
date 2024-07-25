package io.github.hanhuoer.maa.core;

import io.github.hanhuoer.maa.MaaWindowsAarch64LibraryLoader;
import io.github.hanhuoer.maa.loader.LibraryLoader;
import io.github.hanhuoer.maa.callbak.MaaControllerCallback;
import io.github.hanhuoer.maa.model.AdbInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Slf4j
class AdbControllerTest {

    @BeforeAll
    static void init() throws Exception {
        LibraryLoader loader = new MaaWindowsAarch64LibraryLoader();
        loader.loadLibrary();
    }

    private static String bufferedImageToBase64(BufferedImage bufferedImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();

        byte[] encode = Base64.getEncoder().encode(bytes);
        String imgBase64 = new String(encode).trim();
        imgBase64 = imgBase64.replaceAll("\n", "").replaceAll("\r", "");
        return "data:image/jpg;base64," + imgBase64;
    }

    @Test
    void find() {
        List<AdbInfo> adbInfoList = AdbController.find();
        log.info("adb list: {}", adbInfoList);
    }

    @Test
    void testFind() {
        List<AdbInfo> adbInfoList = AdbController.find("C:/Program Files/Tools/adb/platform-tools/adb.exe");
        log.info("adb list: {}", adbInfoList);
    }

    @Test
    void testConnection() {
        List<AdbInfo> adbInfoList = AdbController.find();
        AdbInfo adbInfo = adbInfoList.get(0);

        MaaControllerCallback callback = (msg, detailsJson, callbackArg) -> {
            log.info("received callback: ********************************************");
            log.info("message: {}", msg);
            log.info("detailJson: {}", detailsJson);
            log.info("callbackArg: {}", callbackArg);
        };

        AdbController adbController = new AdbController(adbInfo, callback, null);
        log.info("adb controller: {}", adbController);
        log.info("adb connected: {}", adbController.connected());

        log.info("adb controller start connecting");
        boolean connect = adbController.connect();
        log.info("adb connect result: {}", connect);
        log.info("adb connected: {}", adbController.connected());

        log.info("...");
    }

    @Test
    void testClick() {
        List<AdbInfo> adbInfoList = AdbController.find();
        AdbInfo adbInfo = adbInfoList.get(0);

        MaaControllerCallback callback = (msg, detailsJson, callbackArg) -> {
            log.info("received callback: ********************************************");
            log.info("message: {}", msg);
            log.info("detailJson: {}", detailsJson);
            log.info("callbackArg: {}", callbackArg);
        };

        AdbController adbController = new AdbController(adbInfo, callback, null);
        log.info("adb controller: {}", adbController);
        log.info("adb connected: {}", adbController.connected());

        log.info("adb controller start connecting");
        boolean connect = adbController.connect();
        log.info("adb connect result: {}", connect);
        log.info("adb connected: {}", adbController.connected());

        log.info("adb click result: {}", adbController.click(150, 600));
        log.info("adb click result: {}", adbController.click(250, 600));
        log.info("adb click result: {}", adbController.click(350, 600));
        log.info("adb click result: {}", adbController.click(450, 600));

        log.info("...");
    }

    @Test
    void testSwipe() throws Exception {
        List<AdbInfo> adbInfoList = AdbController.find();
        AdbInfo adbInfo = adbInfoList.get(0);

        MaaControllerCallback callback = (msg, detailsJson, callbackArg) -> {
            log.info("received callback: ********************************************");
            log.info("message: {}", msg);
            log.info("detailJson: {}", detailsJson);
            log.info("callbackArg: {}", callbackArg);
        };

        AdbController adbController = new AdbController(adbInfo, callback, null);
        log.info("adb controller: {}", adbController);
        log.info("adb connected: {}", adbController.connected());

        log.info("adb controller start connecting");
        boolean connect = adbController.connect();
        log.info("adb connect result: {}", connect);
        log.info("adb connected: {}", adbController.connected());

        log.info("adb swipe result: {}", adbController.swipe(450, 100, 450, 600, 1 * 1000));
        log.info("adb swipe result: {}", adbController.swipe(100, 450, 600, 450, 5 * 1000));

        log.info("...");
        Thread.sleep(10 * 1000);
    }

    @Test
    void testScreencap() throws Exception {
        List<AdbInfo> adbInfoList = AdbController.find();
        AdbInfo adbInfo = adbInfoList.get(0);

        MaaControllerCallback callback = (msg, detailsJson, callbackArg) -> {
            log.info("received callback: ********************************************");
            log.info("message: {}", msg);
            log.info("detailJson: {}", detailsJson);
            log.info("callbackArg: {}", callbackArg);
        };

        AdbController adbController = new AdbController(adbInfo, callback, null);
        log.info("adb controller: {}", adbController);
        log.info("adb connected: {}", adbController.connected());

        log.info("adb controller start connecting");
        boolean connect = adbController.connect();
        log.info("adb connect result: {}", connect);
        log.info("adb connected: {}", adbController.connected());

        BufferedImage screencap = adbController.screencap();
        log.info("adb screencap result: {}", screencap);
        String base64Img = bufferedImageToBase64(screencap);
        log.info("adb screencap base64 result: {}", base64Img);

        BufferedImage screencap2 = adbController.screencap(false);
        String base64Img2 = bufferedImageToBase64(screencap2);
        log.info("adb screencap base64 result: {}", base64Img2);

        log.info("...");
        Thread.sleep(10 * 1000);
    }


}