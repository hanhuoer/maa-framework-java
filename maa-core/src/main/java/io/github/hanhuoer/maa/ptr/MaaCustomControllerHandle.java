package io.github.hanhuoer.maa.ptr;

import com.sun.jna.Structure;
import io.github.hanhuoer.maa.callbak.MaaCustomControllerCallback;

import java.util.List;

/**
 * struct MaaCustomControllerAPI;
 * typedef struct MaaCustomControllerAPI* MaaCustomControllerHandle;
 *
 * @author H
 */
public class MaaCustomControllerHandle extends Structure {

    public MaaCustomControllerCallback.Connect connect;
    public MaaCustomControllerCallback.RequestUUID requestUUID;
    public MaaCustomControllerCallback.StartApp startApp;
    public MaaCustomControllerCallback.StopApp stopApp;
    public MaaCustomControllerCallback.Screencap screencap;
    public MaaCustomControllerCallback.Click click;
    public MaaCustomControllerCallback.Swipe swipe;
    public MaaCustomControllerCallback.TouchDown touchDown;
    public MaaCustomControllerCallback.TouchMove touchMove;
    public MaaCustomControllerCallback.TouchUp touchUp;
    public MaaCustomControllerCallback.PressKey pressKey;
    public MaaCustomControllerCallback.InputText inputText;

    @Override
    protected List<String> getFieldOrder() {
        return List.of(
                "connect",
                "requestUUID",
                "startApp",
                "stopApp",
                "screencap",
                "click",
                "swipe",
                "touchDown",
                "touchMove",
                "touchUp",
                "pressKey",
                "inputText"
        );
    }

}