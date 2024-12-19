package io.github.hanhuoer.maa;

import com.sun.jna.Platform;
import io.github.hanhuoer.maa.component.FileVersion;
import io.github.hanhuoer.maa.core.base.Tasker;
import io.github.hanhuoer.maa.exception.LoadException;
import io.github.hanhuoer.maa.exception.MaaFrameworkException;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.jna.MaaToolkit;
import io.github.hanhuoer.maa.loader.AgentLibraryLoader;
import io.github.hanhuoer.maa.loader.MaaLibraryLoader;
import io.github.hanhuoer.maa.util.FileUtils;
import io.github.hanhuoer.maa.util.StringUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author H
 */
@Slf4j
@Getter
public class Maa {

    private static final AtomicBoolean isLibraryLoaded = new AtomicBoolean(false);
    private static volatile Maa INSTANCE;
    private static volatile MaaLibraryLoader nativeLoader;
    private static volatile AgentLibraryLoader agentLoader;

    static {
        System.setProperty("sun.jnu.encoding", StandardCharsets.UTF_8.name());
        System.setProperty("native.encoding", StandardCharsets.UTF_8.name());
    }

    private MaaOptions options;
    private MaaFramework maaFramework;
    private MaaToolkit maaToolkit;

    private Maa() {
    }

    public static Maa create() {
        MaaOptions maaOptions = new MaaOptions();
        return create(maaOptions);
    }

    public static Maa create(MaaOptions options) {
        if (INSTANCE == null) {
            synchronized (Maa.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Maa();

                    INSTANCE.beforeInit(options);
                    INSTANCE.init(options);
                    INSTANCE.afterInit(options);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * shutdown handle.
     *
     * @param options maa options.
     */
    public static void shutdownHandle(MaaOptions options) {
        String osName = System.getProperty("os.name").toLowerCase();
        if (options.getKillAdbOnShutdown()) {

            List<String> commands = new ArrayList<>();
            if (osName.contains("win")) {
                commands.addAll(List.of("taskkill", "/F", "/IM", "adb.exe"));
            } else {
                commands.addAll(List.of("pkill", "adb"));
            }

            log.info("adb killer registered.");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                ProcessBuilder pb = new ProcessBuilder(commands);
                try {
                    log.info("killing adb process.");
                    pb.start();
                    log.info("adb process has been killed.");
                } catch (IOException e) {
                    log.error("Failed to kill adb process: {}", e.getMessage(), e);
                }
            }));
        }
    }

    @SneakyThrows
    public static void loadFileIfNeeded() {
        if (Maa.nativeLoader == null && (isLibraryLoaded.compareAndSet(false, true))) {
            synchronized (Maa.class) {
                if (Maa.nativeLoader == null) {
                    MaaLibraryLoader nativeLoader = findNativeLibLoader();
                    if (nativeLoader == null) {
                        throw new LoadException("Unable to find a suitable native loader implementation. Possible reasons include: " +
                                "1. The Maven coordinates for Maa are not included! " +
                                "2. The runtime library might not yet be adapted for your system: " + System.getProperty("os.name").toLowerCase() + System.getProperty("os.arch").toLowerCase() + "! " +
                                "3. The model used does not match the JAR dependency included, currently used: Maa, please check your JAR dependencies are correct! " +
                                "4. Incorrect inclusion of the runtime library during packaging, such as packaging Windows dependencies but running on Linux, please refer to the documentation to check if the correct packaging command was used!");
                    }
                    nativeLoader.loadLibrary();
                    isLibraryLoaded.set(true);
                    Maa.nativeLoader = nativeLoader;
                }
            }
        }
        log.debug("Current library native loader: {}", nativeLoader.getClass().getSimpleName());

        if (Maa.agentLoader == null) {
            synchronized (Maa.class) {
                if (Maa.agentLoader == null) {
                    AgentLibraryLoader agentLoader = findAgentLibLoader();
                    if (agentLoader == null) {
                        throw new LoadException("Please check whether the agent is in the dependencies.");
                    }
                    agentLoader.loadLibrary();
                    Maa.agentLoader = agentLoader;
                }
            }
        }
        log.debug("Current library agent loader: {}", agentLoader.getClass().getSimpleName());
    }

    private static AgentLibraryLoader findAgentLibLoader() {
        Properties props = new Properties();
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("load/loaders.properties")) {
            if (input == null) {
                throw new FileNotFoundException("Not found in loaders.properties");
            }
            props.load(input);

            String loaderClassName = props.getProperty("maa.agent");

            if (loaderClassName != null) {
                return (AgentLibraryLoader) Class.forName(loaderClassName).getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            log.error("Failed to get agent loader: {}", e.getMessage());
        }
        return null;
    }

    private static MaaLibraryLoader findNativeLibLoader() {
        Properties props = new Properties();
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("load/loaders.properties")) {
            if (input == null) {
                throw new FileNotFoundException("Not found in loaders.properties");
            }
            props.load(input);

            String osName = System.getProperty("os.name").toLowerCase();
            String osArch = System.getProperty("os.arch").toLowerCase();
            log.debug("osName: {}, osArch: {}", osName, osArch);
            String loaderClassName = null;
            if (osName.contains("win")) {
                if (osArch.contains("x86") || osArch.contains("amd64")) {
                    loaderClassName = props.getProperty("maa.win-x86_64");
                } else if (osArch.contains("arch64")) {
                    loaderClassName = props.getProperty("maa.win-aarch64");
                }
            } else if (osName.contains("mac")) {
                if (osArch.contains("arch64")) {
                    loaderClassName = props.getProperty("maa.mac-aarch64");
                } else {
                    loaderClassName = props.getProperty("maa.mac-x86_64");
                }
            } else if (osName.contains("linux")) {
                String env;
                if (checkIfAndroid()) {
                    env = "android";
                } else {
                    env = "linux";
                }

                if (osArch.contains("x86") || osArch.contains("amd64")) {
                    loaderClassName = props.getProperty(String.format("maa.%s-x86_64", env));
                } else if (osArch.contains("arm") || osArch.contains("arch64")) {
                    loaderClassName = props.getProperty(String.format("maa.%s-aarch64", env));
                }
            }
            if (loaderClassName != null) {
                return (MaaLibraryLoader) Class.forName(loaderClassName).getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            log.error("Failed to get native loader: {}", e.getMessage());
        }
        return null;
    }

    /**
     * <a href="https://developer.android.com/reference/java/lang/System#getProperties()">...</a>
     *
     * @return true if android.
     */
    private static boolean checkIfAndroid() {

        try {
            Class.forName("android.os.Build");
            return true;
        } catch (ClassNotFoundException e) {
            // return false;
        }

        List<String> propertyList = List.of(
                "java.vendor",
                "java.vendor.url",
                "java.specification.vendor",
                "java.vm.vendor",
                "java.vm.specification.vendor",
                "java.home",
                "java.io.tmpdir"
        );
        for (String property : propertyList) {
            if (Optional.ofNullable(System.getProperty(property))
                    .orElse("")
                    .toLowerCase()
                    .contains("android")) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check whether the file version of msvcp140 meets the requirements.
     */
    private static void checkWindowsMsvcpVersion() {
        if (!Platform.isWindows()) {
            return;
        }
        String javaHome = System.getProperty("java.home");
        String javaDllPath = FileUtils.join(javaHome, "bin", "msvcp140.dll").getAbsolutePath();
        if (FileUtils.exists(javaDllPath)) {
            String version = FileVersion.getVersion(javaDllPath);
            if (version == null) {
                log.warn("Please check the msvcp140.dll file: {}", javaDllPath);
                return;
            }

            if (StringUtils.compareVersion(version, "14.40.0.0") < 0) {
                throw new MaaFrameworkException("Please check the msvcp140.dll file version is greater than 14.40, refer to https://github.com/MaaXYZ/MaaFramework/issues/361 for details");
            }

        } else {
            String sysDllPath = null;
            if (Platform.is64Bit()) {
                sysDllPath = "C:\\Windows\\System32\\msvcp140.dll";
            } else {
                sysDllPath = "C:\\Windows\\SysWOW64\\msvcp140.dll";
            }
            if (!FileUtils.exists(sysDllPath)) {
                throw new MaaFrameworkException("Please check the msvcp140.dll file if exists.");
            }

            String version = FileVersion.getVersion(sysDllPath);
            if (version == null) {
                log.warn("Please check the msvcp140.dll file: {}", sysDllPath);
                return;
            }

            if (StringUtils.compareVersion(version, "14.40.0.0") < 0) {
                throw new MaaFrameworkException("Please check the msvcp140.dll file version is greater than 14.40, refer to https://github.com/MaaXYZ/MaaFramework/issues/361 for details");
            }
        }
    }

    /**
     * 初始化操作
     *
     * @param options maa options
     */
    private void init(MaaOptions options) {
        loadFileIfNeeded();

        this.options = options;
        this.maaFramework = MaaFramework.create(nativeLoader, options);
        this.maaToolkit = MaaToolkit.create(nativeLoader, options);

        loadGlobalSetting();

        log.info("Maa {} created successfully.", version());
    }

    private void beforeInit(MaaOptions options) {
        checkWindowsMsvcpVersion();
    }

    /**
     * 设置接口 MaaSetGlobalOption 提供的选项
     */
    private void loadGlobalSetting() {
        if (StringUtils.hasText(options.getLogDir())) {
            boolean result = Tasker.setLogDir(options.getLogDir());
            log.info("[maa] set logDir: {}, result: {}", options.getLogDir(), result);
        }
        if (options.getRecording() != null) {
            boolean result = Tasker.setRecording(options.getRecording());
            log.info("[maa] set recording: {}, result: {}", options.getRecording(), result);
        }
        if (options.getDebugMessage() != null) {
            boolean result = Tasker.setDebugMessage(options.getDebugMessage());
            log.info("[maa] set debugMessage: {}, result: {}", options.getDebugMessage(), result);
        }
        if (options.getSaveDraw() != null) {
            boolean result = Tasker.setSaveDraw(options.getSaveDraw());
            log.info("[maa] set saveDraw: {}, result: {}", options.getSaveDraw(), result);
        }
        if (options.getStdoutLevel() != null) {
            boolean result = Tasker.setStdoutLevel(options.getStdoutLevel());
            log.info("[maa] set stdoutLevel: {}, result: {}", options.getStdoutLevel(), result);
        }
        if (options.getShowHitDraw() != null) {
            boolean result = Tasker.setShowHitDraw(options.getShowHitDraw());
            log.info("[maa] set showHitDraw: {}, result: {}", options.getShowHitDraw(), result);
        }
    }

    public String version() {
        return maaFramework.getUtility().MaaVersion();
    }

    private void afterInit(MaaOptions options) {
        shutdownHandle(options);
    }
}
