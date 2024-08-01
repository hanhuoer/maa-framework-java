package io.github.hanhuoer.maa.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import io.github.hanhuoer.maa.MaaOptions;
import io.github.hanhuoer.maa.loader.MaaLibraryLoader;
import io.github.hanhuoer.maa.model.AdbInfo;
import io.github.hanhuoer.maa.ptr.MaaInstanceHandle;
import io.github.hanhuoer.maa.ptr.MaaStringBufferHandle;
import io.github.hanhuoer.maa.ptr.MaaWin32Hwnd;
import io.github.hanhuoer.maa.util.FileUtils;

/**
 * @author H
 */
public class MaaToolkit {

    private static volatile MaaToolkit INSTANCE;

    private MaaToolkitConfig config;
    private MaaToolkitDevice device;
    private MaaToolkitExecAgent execAgent;
    private MaaToolkitWin32Window win32Window;


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

    public static AdbInfo getAdbDevice(Long index) {
        checkMaaToolKit();

        String deviceAdbPath = INSTANCE.device.MaaToolkitGetDeviceAdbPath(index);
        String deviceAdbSerial = INSTANCE.device.MaaToolkitGetDeviceAdbSerial(index);
        String deviceAdbConfig = INSTANCE.device.MaaToolkitGetDeviceAdbConfig(index);
        String deviceName = INSTANCE.device.MaaToolkitGetDeviceName(index);
        Integer deviceAdbControllerType = INSTANCE.device.MaaToolkitGetDeviceAdbControllerType(index);

        return new AdbInfo()
                .setPath(deviceAdbPath)
                .setSerial(deviceAdbSerial)
                .setConfig(deviceAdbConfig)
                .setName(deviceName)
                .setControllerType(deviceAdbControllerType);
    }

    public static MaaToolkitConfig config() {
        checkMaaToolKit();
        return INSTANCE.config;
    }

    public static MaaToolkitDevice device() {
        checkMaaToolKit();
        return INSTANCE.device;
    }

    public static MaaToolkitExecAgent execAgent() {
        checkMaaToolKit();
        return INSTANCE.execAgent;
    }

    public static MaaToolkitWin32Window win32Window() {
        checkMaaToolKit();
        return INSTANCE.win32Window;
    }

    public static void checkMaaToolKit() {
        if (INSTANCE == null) {
            throw new RuntimeException("MaaToolKit instance can not be null.");
        }
        if (INSTANCE.config == null) {
            throw new RuntimeException("MaaToolKit.config can not be null.");
        }
        if (INSTANCE.device == null) {
            throw new RuntimeException("MaaToolKit.device can not be null.");
        }
        if (INSTANCE.execAgent == null) {
            throw new RuntimeException("MaaToolKit.execAgent can not be null.");
        }
        if (INSTANCE.win32Window == null) {
            throw new RuntimeException("MaaToolKit.win32Window can not be null.");
        }
    }

    private void init(MaaLibraryLoader nativeLoader, MaaOptions options) {
        String libPath = FileUtils.join(options.getLibDir(), nativeLoader.getMaaToolkitName()).getAbsolutePath();
        this.config = Native.load(libPath, MaaToolkitConfig.class);
        this.device = Native.load(libPath, MaaToolkitDevice.class);
        this.execAgent = Native.load(libPath, MaaToolkitExecAgent.class);
        this.win32Window = Native.load(libPath, MaaToolkitWin32Window.class);
    }

    /**
     * MaaToolkitConfig.h File Reference
     *
     * @link <a href="https://maaxyz.github.io/MaaFramework/MaaToolkitConfig_8h.html">...</a>
     */
    public interface MaaToolkitConfig extends Library {

        /**
         * @param userPath user path
         * @param param    value like "{}"
         */
        Boolean MaaToolkitInitOptionConfig(String userPath, String param);

        /**
         * @deprecated
         */
        Boolean MaaToolkitInit();

        /**
         * @deprecated don't use it.
         */
        Boolean MaaToolkitUninit();

    }


    /**
     * MaaToolkitDevice.h File Reference
     *
     * @link <a href="https://maaxyz.github.io/MaaFramework/MaaToolkitDevice_8h.html">...</a>
     */
    public interface MaaToolkitDevice extends Library {

        Long MaaToolkitFindDevice();

        Long MaaToolkitFindDeviceWithAdb(String adb_path);

        Boolean MaaToolkitPostFindDevice();

        Boolean MaaToolkitPostFindDeviceWithAdb(String adb_path);

        Boolean MaaToolkitIsFindDeviceCompleted();

        Long MaaToolkitWaitForFindDeviceToComplete();

        Long MaaToolkitGetDeviceCount();

        String MaaToolkitGetDeviceName(Long index);

        String MaaToolkitGetDeviceAdbPath(Long index);

        String MaaToolkitGetDeviceAdbSerial(Long index);

        /**
         * @return MaaAdbControllerType
         */
        Integer MaaToolkitGetDeviceAdbControllerType(Long index);

        String MaaToolkitGetDeviceAdbConfig(Long index);

    }

    /**
     * MaaToolkitExecAgent.h File Reference
     *
     * @link <a href="https://maaxyz.github.io/MaaFramework/MaaToolkitExecAgent_8h.html">...</a>
     */
    public interface MaaToolkitExecAgent extends Library {

        Boolean MaaToolkitRegisterCustomRecognizerExecutor(MaaInstanceHandle handle, String recognizer_name, String exec_path, String[] exec_params, long exec_param_size);

        Boolean MaaToolkitUnregisterCustomRecognizerExecutor(MaaInstanceHandle handle, String recognizer_name);

        Boolean MaaToolkitClearCustomRecognizerExecutor(MaaInstanceHandle handle);

        Boolean MaaToolkitRegisterCustomActionExecutor(MaaInstanceHandle handle, String action_name, String exec_path, String[] exec_params, long exec_param_size);

        Boolean MaaToolkitUnregisterCustomActionExecutor(MaaInstanceHandle handle, String action_name);

        Boolean MaaToolkitClearCustomActionExecutor(MaaInstanceHandle handle);

    }

    /**
     * MaaToolkitWin32Window.h File Reference
     *
     * @link <a href="https://maaxyz.github.io/MaaFramework/MaaToolkitWin32Window_8h.html">...</a>
     */
    public interface MaaToolkitWin32Window extends Library {

        /**
         * Find a win32 window by class name and window name.
         * <p>
         * This function finds the function by exact match. See also MaaToolkitSearchWindow().
         *
         * @param class_name  The class name of the window. If passed an empty string, class name will not be filtered.
         * @param window_name The window name of the window. If passed an empty string, window name will not be filtered.
         * @return Integer The number of windows found that match the criteria. To get the corresponding
         * window handle, use MaaToolkitGetWindow().
         */
        Integer MaaToolkitFindWindow(String class_name, String window_name);

        /**
         * Regex search a win32 window by class name and window name.
         * <p>
         * This function searches the function by regex search. See also MaaToolkitFindWindow().
         *
         * @param class_name  The class name regex of the window. If passed an empty string, class name will not be filtered.
         * @param window_name The window name regex of the window. If passed an empty string, window name will not be filtered.
         * @return Integer The number of windows found that match the criteria. To get the corresponding
         * window handle, use MaaToolkitGetWindow().
         */
        Integer MaaToolkitSearchWindow(String class_name, String window_name);

        /**
         * List all windows.
         *
         * @return Integer The number of windows found. To get the corresponding window handle, use
         */
        Integer MaaToolkitListWindows();

        /**
         * Get the window handle by index.
         *
         * @param index The 0-based index of the window. The index should not exceed the number of
         *              windows found otherwise out_of_range exception will be thrown.
         * @return MaaWin32Hwnd The window handle.
         */
        MaaWin32Hwnd MaaToolkitGetWindow(Integer index);

        /**
         * Get the window handle of the window under the cursor. This uses the WindowFromPoint()
         *
         * @return MaaWin32Hwnd The window handle.
         * <p>
         * system API.
         */
        MaaWin32Hwnd MaaToolkitGetCursorWindow();

        /**
         * Get the desktop window handle. This uses the GetDesktopWindow() system API.
         *
         * @return MaaWin32Hwnd The window handle.
         */
        MaaWin32Hwnd MaaToolkitGetDesktopWindow();

        /**
         * Get the foreground window handle. This uses the GetForegroundWindow() system API.
         *
         * @return MaaWin32Hwnd The window handle.
         */
        MaaWin32Hwnd MaaToolkitGetForegroundWindow();

        /**
         * Get the window class name by hwnd.
         *
         * @param hwnd   The window hwnd.
         * @param buffer The output buffer.
         * @return Boolean.
         */
        Boolean MaaToolkitGetWindowClassName(MaaWin32Hwnd hwnd, MaaStringBufferHandle buffer);

        /**
         * Get the window window name by hwnd.
         *
         * @param hwnd   The window hwnd.
         * @param buffer The output buffer.
         * @return Boolean.
         */
        Boolean MaaToolkitGetWindowWindowName(MaaWin32Hwnd hwnd, MaaStringBufferHandle buffer);

    }

}