package io.github.hanhuoer.maa.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import io.github.hanhuoer.maa.MaaOptions;
import io.github.hanhuoer.maa.callbak.MaaCustomActionCallback;
import io.github.hanhuoer.maa.callbak.MaaCustomRecognitionCallback;
import io.github.hanhuoer.maa.callbak.MaaNotificationCallback;
import io.github.hanhuoer.maa.define.*;
import io.github.hanhuoer.maa.define.base.MaaBool;
import io.github.hanhuoer.maa.loader.MaaLibraryLoader;
import io.github.hanhuoer.maa.util.FileUtils;

/**
 * @author H
 */
public class MaaToolkit {

    private static volatile MaaToolkit INSTANCE;

    private MaaToolkitConfig config;
    private MaaToolkitAdbDevice adbDevice;
    private MaaToolkitProjectInterface projectInterface;
    private MaaToolkitDesktopWindow desktopWindow;


    private MaaToolkit() {
    }

    public static MaaToolkit create(MaaLibraryLoader nativeLoader, MaaOptions options) {
        if (INSTANCE == null) {
            synchronized (MaaToolkit.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MaaToolkit();

                    INSTANCE.init(nativeLoader, options);
                }
            }
        }
        return INSTANCE;
    }

    public static MaaToolkitConfig config() {
        checkMaaToolKit();
        return INSTANCE.config;
    }

    public static MaaToolkitAdbDevice device() {
        checkMaaToolKit();
        return INSTANCE.adbDevice;
    }

    public static MaaToolkitProjectInterface projectInterface() {
        checkMaaToolKit();
        return INSTANCE.projectInterface;
    }

    public static MaaToolkitDesktopWindow desktopWindow() {
        checkMaaToolKit();
        return INSTANCE.desktopWindow;
    }

    public static void checkMaaToolKit() {
        if (INSTANCE == null) {
            throw new RuntimeException("MaaToolKit instance can not be null.");
        }
        if (INSTANCE.config == null) {
            throw new RuntimeException("MaaToolKit.config can not be null.");
        }
        if (INSTANCE.adbDevice == null) {
            throw new RuntimeException("MaaToolKit.device can not be null.");
        }
        if (INSTANCE.projectInterface == null) {
            throw new RuntimeException("MaaToolKit.execAgent can not be null.");
        }
        if (INSTANCE.desktopWindow == null) {
            throw new RuntimeException("MaaToolKit.win32Window can not be null.");
        }
    }

    private void init(MaaLibraryLoader nativeLoader, MaaOptions options) {
        String libPath = FileUtils.join(options.getLibDir(), nativeLoader.getMaaToolkitName()).getAbsolutePath();
        this.config = Native.load(libPath, MaaToolkitConfig.class);
        this.adbDevice = Native.load(libPath, MaaToolkitAdbDevice.class);
        this.projectInterface = Native.load(libPath, MaaToolkitProjectInterface.class);
        this.desktopWindow = Native.load(libPath, MaaToolkitDesktopWindow.class);
    }

    /**
     * MaaToolkitConfig.h File Reference
     *
     * @link <a href="https://maaxyz.github.io/MaaFramework/MaaToolkitConfig_8h.html">...</a>
     */
    public interface MaaToolkitConfig extends Library {

        MaaBool MaaToolkitConfigInitOption(String user_path, String default_json);

    }

    /**
     * MaaToolkitAdbDevice.h File Reference
     *
     * @link <a href="https://maaxyz.github.io/MaaFramework/MaaToolkitAdbDevice_8h.html">...</a>
     */
    public interface MaaToolkitAdbDevice extends Library {

        MaaToolkitAdbDeviceListHandle MaaToolkitAdbDeviceListCreate();

        void MaaToolkitAdbDeviceListDestroy(MaaToolkitAdbDeviceListHandle handle);

        /**
         * @param buffer out
         */
        MaaBool MaaToolkitAdbDeviceFind(MaaToolkitAdbDeviceListHandle buffer);

        /**
         * @param buffer out
         */
        MaaBool MaaToolkitAdbDeviceFindSpecified(String adb_path, MaaToolkitAdbDeviceListHandle buffer);

        MaaSize MaaToolkitAdbDeviceListSize(MaaToolkitAdbDeviceListHandle list);

        MaaToolkitAdbDeviceHandle MaaToolkitAdbDeviceListAt(MaaToolkitAdbDeviceListHandle list, MaaSize index);

        String MaaToolkitAdbDeviceGetName(MaaToolkitAdbDeviceHandle device);

        String MaaToolkitAdbDeviceGetAdbPath(MaaToolkitAdbDeviceHandle device);

        String MaaToolkitAdbDeviceGetAddress(MaaToolkitAdbDeviceHandle device);

        MaaAdbScreencapMethod MaaToolkitAdbDeviceGetScreencapMethods(MaaToolkitAdbDeviceHandle device);

        MaaAdbInputMethod MaaToolkitAdbDeviceGetInputMethods(MaaToolkitAdbDeviceHandle device);

        String MaaToolkitAdbDeviceGetConfig(MaaToolkitAdbDeviceHandle device);

    }

    /**
     * MaaToolkitProjectInterface.h File Reference
     *
     * @link <a href="https://maaxyz.github.io/MaaFramework/MaaToolkitProjectInterface_8h.html">...</a>
     */
    public interface MaaToolkitProjectInterface extends Library {

        void MaaToolkitProjectInterfaceRegisterCustomRecognition(long inst_id, String name,
                                                                 MaaCustomRecognitionCallback recognizer,
                                                                 MaaTransparentArg trans_arg);

        void MaaToolkitProjectInterfaceRegisterCustomAction(long inst_id, String name,
                                                            MaaCustomActionCallback action,
                                                            MaaTransparentArg trans_arg);

        MaaBool MaaToolkitProjectInterfaceRunCli(long inst_id,
                                                 String resource_path, String user_path,
                                                 MaaBool directly,
                                                 MaaNotificationCallback callback,
                                                 MaaCallbackTransparentArg callback_arg);

    }

    /**
     * MaaToolkitDesktopWindow.h File Reference
     *
     * @link <a href="https://maaxyz.github.io/MaaFramework/MaaToolkitDesktopWindow_8h.html">...</a>
     */
    public interface MaaToolkitDesktopWindow extends Library {

        MaaToolkitDesktopWindowListHandle MaaToolkitDesktopWindowListCreate();

        void MaaToolkitDesktopWindowListDestroy(MaaToolkitDesktopWindowListHandle handle);

        /**
         * @param buffer out
         */
        MaaBool MaaToolkitDesktopWindowFindAll(MaaToolkitDesktopWindowListHandle buffer);

        MaaSize MaaToolkitDesktopWindowListSize(MaaToolkitDesktopWindowListHandle list);

        MaaToolkitDesktopWindowHandle MaaToolkitDesktopWindowListAt(MaaToolkitDesktopWindowListHandle list, MaaSize index);

        void MaaToolkitDesktopWindowGetHandle(MaaToolkitDesktopWindowHandle window);

        String MaaToolkitDesktopWindowGetClassName(MaaToolkitDesktopWindowHandle window);

        String MaaToolkitDesktopWindowGetWindowName(MaaToolkitDesktopWindowHandle window);


    }

}