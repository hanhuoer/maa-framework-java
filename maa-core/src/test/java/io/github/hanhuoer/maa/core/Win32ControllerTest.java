package io.github.hanhuoer.maa.core;

import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.model.Win32Info;
import io.github.hanhuoer.maa.ptr.MaaWin32Hwnd;
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
    void find() {

    }

    @Test
    void search() {
        Integer index = Win32Controller.search("Notepad", "");
        log.info("search result index: {}", index);
        MaaWin32Hwnd maaWin32Hwnd = Win32Controller.get(index);
        Win32Info info = Win32Controller.getInfo(maaWin32Hwnd);
        log.info("search info: {}", info);
    }

    @Test
    void list() {
        Integer listCount = Win32Controller.list();
        log.info("list count: {}", listCount);
    }

    @Test
    void get() {
    }

    @Test
    void getCursor() {
    }

    @Test
    void getDesktop() {
    }

    @Test
    void getForeground() {
    }

    @Test
    void getInfo() {
    }

    @Test
    void listInfo() {
        List<Win32Info> infoList = Win32Controller.listInfo();
        log.info("info size: {}", infoList.size());
    }

    @Test
    void screencapTest() {
        List<Win32Info> infoList = Win32Controller.listInfo();
        Win32Info info = infoList.stream().filter(item -> item.getClassName().contains("Notepad")).findFirst().orElse(null);
        if (info == null) return;

        Win32Controller controller = new Win32Controller(info.getHwnd(), null, null, null, null, null);
        BufferedImage screencap = controller.screencap();
        String base64 = ImageUtils.toBase64(screencap);
        log.info("win32 screencap: {}", base64);
    }
}