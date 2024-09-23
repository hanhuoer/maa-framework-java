package io.github.hanhuoer.maa.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import io.github.hanhuoer.maa.MaaAndroidAarch64LibraryLoader;
import io.github.hanhuoer.maa.MaaLinuxAarch64LibraryLoader;
import io.github.hanhuoer.maa.MaaMacAarch64LibraryLoader;
import io.github.hanhuoer.maa.MaaWindowsAarch64LibraryLoader;
import io.github.hanhuoer.maa.loader.MaaLibraryLoader;
import io.github.hanhuoer.maa.ptr.MaaToolkitAdbDeviceListHandle;
import io.github.hanhuoer.maa.util.FileUtils;

public class MaaFrameworkTest {

    public static final String LIB_1_8_9 = FileUtils.joinUserDir("maa-libs", "1.8.9").getAbsolutePath();
    public static final String LIB_2_0_0 = FileUtils.joinUserDir("maa-libs", "2.0.0").getAbsolutePath();

    public static void main(String[] args) {
        System.setProperty("user.language", "en");
        System.setProperty("jna.debug_load", "true");
        System.setProperty("jna.debug_load.jna", "true");
//        System.setProperty("jna.library.path", LIB_1_8_9);
//        System.setProperty("jna.library.path", LIB_2_0_0);

//        System.out.println(MaaController1_8_9.INSTANCE);
//        System.out.println(MaaController2_0_0.INSTANCE);
//        System.out.println(MaaToolkit1_8_9.INSTANCE);
//        System.out.println(MaaToolkit2_0_0.INSTANCE);
//        System.load(MaaController1_8_9.LIB);
//        System.load(MaaController2_0_0.LIB);
//        System.load(MaaToolkit1_8_9.LIB);
//        System.load(MaaToolkit2_0_0.LIB);
//        System.loadLibrary("MaaFramework");
//        System.loadLibrary("MaaToolkit");
    }

    public static MaaLibraryLoader getNativeLoader() {
        if (Platform.isWindows()) {
            return new MaaWindowsAarch64LibraryLoader();
        } else if (Platform.isLinux()) {
            return new MaaLinuxAarch64LibraryLoader();
        } else if (Platform.isMac()) {
            return new MaaMacAarch64LibraryLoader();
        } else if (Platform.isAndroid()) {
            return new MaaAndroidAarch64LibraryLoader();
        } else {
            throw new RuntimeException("err");
        }
    }

    public interface MaaController1_8_9 extends Library {
        String LIB = FileUtils.join(LIB_1_8_9, getNativeLoader().getMaaFrameworkName()).getAbsolutePath();
        MaaController1_8_9 INSTANCE = Native.load(LIB, MaaController1_8_9.class);

    }

    public interface MaaController2_0_0 extends Library {
        String LIB = FileUtils.join(LIB_2_0_0, getNativeLoader().getMaaFrameworkName()).getAbsolutePath();
        MaaController2_0_0 INSTANCE = Native.load(LIB, MaaController2_0_0.class);

    }

    public interface MaaToolkit1_8_9 extends Library {
        String LIB = FileUtils.join(LIB_1_8_9, getNativeLoader().getMaaToolkitName()).getAbsolutePath();
        MaaToolkit1_8_9 INSTANCE = Native.load(LIB, MaaToolkit1_8_9.class);

    }


    public interface MaaToolkit2_0_0 extends Library {
        String LIB = FileUtils.join(LIB_2_0_0, getNativeLoader().getMaaToolkitName()).getAbsolutePath();
        MaaToolkit2_0_0 INSTANCE = Native.load(LIB, MaaToolkit2_0_0.class);

        MaaToolkitAdbDeviceListHandle MaaToolkitAdbDeviceListCreate();

    }

}
