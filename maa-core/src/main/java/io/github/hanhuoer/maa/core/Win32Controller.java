package io.github.hanhuoer.maa.core;

import io.github.hanhuoer.maa.callbak.MaaControllerCallback;
import io.github.hanhuoer.maa.consts.MaaWin32ControllerTypeEnum;
import io.github.hanhuoer.maa.core.base.Controller;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.jna.MaaToolkit;
import io.github.hanhuoer.maa.model.Win32Info;
import io.github.hanhuoer.maa.ptr.MaaControllerHandle;
import io.github.hanhuoer.maa.ptr.MaaWin32Hwnd;
import io.github.hanhuoer.maa.ptr.StringBuffer;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author H
 */
@Slf4j
public class Win32Controller extends Controller {

    public Win32Controller(
            MaaWin32Hwnd hWnd,
            MaaWin32ControllerTypeEnum touchType,
            MaaWin32ControllerTypeEnum keyType,
            MaaWin32ControllerTypeEnum screencapType,
            MaaControllerCallback callback, Object callbackArgs
    ) {
        super();

        if (touchType == null) touchType = MaaWin32ControllerTypeEnum.TOUCH_SEIZE;
        if (keyType == null) keyType = MaaWin32ControllerTypeEnum.KEY_SEIZE;
        if (screencapType == null) screencapType = MaaWin32ControllerTypeEnum.SCREENCAP_DXGI_DESKTOPDUP;

        MaaControllerHandle handle = MaaFramework.controller().MaaWin32ControllerCreate(
                hWnd,
                touchType.value | keyType.value | screencapType.value,
                callback, null
        );

        if (handle == null) {
            throw new RuntimeException("Win32Controller create failed");
        }
        super.handle = handle;
    }

    public static Integer find() {
        return find("", "");
    }

    public static Integer find(String className) {
        return find(className, "");
    }


    /**
     * @param className  can be empty, but cannot be null
     * @param windowName can be empty, but cannot be null
     * @return
     */
    public static Integer find(String className, String windowName) {
        return MaaToolkit.win32Window().MaaToolkitFindWindow(className, windowName);
    }

    public static Integer search() {
        return search("", "");
    }

    public static Integer search(String classNameRegex) {
        return search(classNameRegex, "");
    }

    public static Integer search(String classNameRegex, String windowNameRegex) {
        return MaaToolkit.win32Window().MaaToolkitSearchWindow(classNameRegex, windowNameRegex);
    }

    public static Integer list() {
        return MaaToolkit.win32Window().MaaToolkitListWindows();
    }

    public static MaaWin32Hwnd get(int index) {
        return MaaToolkit.win32Window().MaaToolkitGetWindow(index);
    }

    public static Object getCursor() {
        return MaaToolkit.win32Window().MaaToolkitGetCursorWindow();
    }

    public static Object getDesktop() {
        return MaaToolkit.win32Window().MaaToolkitGetDesktopWindow();
    }

    public static Object getForeground() {
        return MaaToolkit.win32Window().MaaToolkitGetForegroundWindow();
    }

    public static Win32Info getInfo(MaaWin32Hwnd hwnd) {
        String className = getClassName(hwnd);
        String windowName = getWindowName(hwnd);
        return new Win32Info()
                .setClassName(className)
                .setWindowName(windowName)
                .setHwnd(hwnd);
    }

    public static List<Win32Info> listInfo() {
        Integer count = list();
        if (count == null || count <= 0) return Collections.emptyList();
        return IntStream.range(0, count)
                .mapToObj(i -> getInfo(MaaToolkit.win32Window().MaaToolkitGetWindow(i)))
                .collect(Collectors.toList());
    }

    private static String getClassName(MaaWin32Hwnd hwnd) {
        try (StringBuffer classNameBuffer = new StringBuffer()) {
            MaaToolkit.win32Window().MaaToolkitGetWindowClassName(hwnd, classNameBuffer.getHandle());
            return classNameBuffer.getValue();
        } catch (Exception e) {
            return null;
        }
    }

    private static String getWindowName(MaaWin32Hwnd hwnd) {
        try (StringBuffer windowNameBuffer = new StringBuffer()) {
            MaaToolkit.win32Window().MaaToolkitGetWindowWindowName(hwnd, windowNameBuffer.getHandle());
            return windowNameBuffer.getValue();
        } catch (Exception e) {
            return null;
        }
    }


}
