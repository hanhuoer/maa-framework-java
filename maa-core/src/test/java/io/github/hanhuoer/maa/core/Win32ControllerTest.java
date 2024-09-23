package io.github.hanhuoer.maa.core;

import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.model.Win32Info;
import io.github.hanhuoer.maa.util.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.List;

@Slf4j
class Win32ControllerTest {

    private Maa maa;

    @BeforeEach
    void setUp() {
        maa = Maa.create();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void listInfo() {
        List<Win32Info> infoList = Win32Controller.listInfo();
        log.info("info size: {}", infoList.size());
        for (int i = 0; i < infoList.size(); i++) {
            log.info("{} - info: {}", i, infoList.get(i));
        }
    }

    @Test
    void screencapTest() {
        List<Win32Info> infoList = Win32Controller.listInfo();
        Win32Info info = infoList.stream().filter(item -> item.getClassName().contains("Notepad")).findFirst().orElse(null);
        if (info == null) return;

        Win32Controller controller = new Win32Controller(info.getHandle(), null, null, null, null);
        BufferedImage screencap = controller.screencap();
        String base64 = ImageUtils.toBase64(screencap);
        log.info("win32 screencap: {}", base64);
    }
}