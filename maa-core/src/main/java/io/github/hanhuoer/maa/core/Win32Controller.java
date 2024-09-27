package io.github.hanhuoer.maa.core;

import io.github.hanhuoer.maa.callbak.MaaNotificationCallback;
import io.github.hanhuoer.maa.consts.MaaWin32InputMethodEnum;
import io.github.hanhuoer.maa.consts.MaaWin32ScreencapMethodEnum;
import io.github.hanhuoer.maa.core.base.Controller;
import io.github.hanhuoer.maa.define.*;
import io.github.hanhuoer.maa.define.base.MaaBool;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.jna.MaaToolkit;
import io.github.hanhuoer.maa.model.Win32Info;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @author H
 */
@Slf4j
public class Win32Controller extends Controller {

    public Win32Controller(
            MaaToolkitDesktopWindowHandle hWnd,
            MaaWin32ScreencapMethodEnum screencapMethod,
            MaaWin32InputMethodEnum inputMethod,
            MaaNotificationCallback callback, MaaCallbackTransparentArg callbackArgs
    ) {
        super();

        if (screencapMethod == null) screencapMethod = MaaWin32ScreencapMethodEnum.DXGI_DESKTOP_DUP;
        if (inputMethod == null) inputMethod = MaaWin32InputMethodEnum.SEIZE;

        MaaControllerHandle handle = MaaFramework.controller().MaaWin32ControllerCreate(
                hWnd,
                MaaWin32ScreencapMethod.valueOf(screencapMethod),
                MaaWin32InputMethod.valueOf(inputMethod),
                callback, callbackArgs
        );

        if (handle == null) {
            throw new RuntimeException("Win32Controller create failed");
        }
        super.handle = handle;
    }

    public static Win32Info getInfo(MaaToolkitDesktopWindowHandle handle) {
        String className = getClassName(handle);
        String windowName = getWindowName(handle);
        return new Win32Info()
                .setClassName(className)
                .setWindowName(windowName)
                .setHandle(handle);
    }

    public static List<Win32Info> listInfo() {
        MaaToolkitDesktopWindowListHandle buffer = MaaToolkit.desktopWindow().MaaToolkitDesktopWindowListCreate();
        MaaBool maaBool = MaaToolkit.desktopWindow().MaaToolkitDesktopWindowFindAll(buffer);

        try {
            if (MaaBool.FALSE.equals(maaBool)) {
                return Collections.emptyList();
            }
            MaaSize maaSize = MaaToolkit.desktopWindow().MaaToolkitDesktopWindowListSize(buffer);
            if (maaSize.getValue() <= 0) {
                return Collections.emptyList();
            }


            return LongStream.range(0, maaSize.getValue())
                    .mapToObj(i -> getInfo(MaaToolkit.desktopWindow().MaaToolkitDesktopWindowListAt(buffer, MaaSize.valueOf(i))))
                    .collect(Collectors.toList());
        } finally {
            MaaToolkit.desktopWindow().MaaToolkitDesktopWindowListDestroy(buffer);
        }
    }

    private static String getClassName(MaaToolkitDesktopWindowHandle handle) {
        return MaaToolkit.desktopWindow().MaaToolkitDesktopWindowGetClassName(handle);
    }

    private static String getWindowName(MaaToolkitDesktopWindowHandle handle) {
        return MaaToolkit.desktopWindow().MaaToolkitDesktopWindowGetWindowName(handle);
    }


}
