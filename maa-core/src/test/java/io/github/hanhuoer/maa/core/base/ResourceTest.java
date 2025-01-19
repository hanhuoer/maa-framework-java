package io.github.hanhuoer.maa.core.base;

import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.buffer.StringListBuffer;
import io.github.hanhuoer.maa.jna.MaaFramework;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.List;

@Slf4j
class ResourceTest {

    private Resource resource;
    private Maa maa;


    @BeforeAll
    static void init() throws Exception {
//        LibraryLoader loader = new MaaWindowsAarch64LibraryLoader();
//        loader.loadLibrary();
    }

    @BeforeEach
    void setUp() {
        maa = Maa.create();

        resource = new Resource((msg, detailsJson, callbackArg) -> {
            log.info("received callback: ********************************************");
            log.info("message: {}", msg);
            log.info("detailJson: {}", detailsJson);
            log.info("callbackArg: {}", callbackArg);
        }, null);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (resource != null) {
            resource.close();
        }
    }

    @Test
    void load() {
        Boolean load = resource.load("./resources/resource");
        log.info("load result: {}", load);
        Assertions.assertTrue(load);
        Assertions.assertTrue(resource.loaded());
    }

    @Test
    void loaded() {
        log.info("loaded: {}", resource.loaded());

        Boolean load = resource.load("./resources/resource");
        log.info("load result: {}", load);

        log.info("loaded: {}", resource.loaded());
    }

    @Test
    void loadAndGetTest() {
        Boolean load = resource.load("./resources/resource");

        try (StringListBuffer stringBuffer = new StringListBuffer()) {
            Boolean getResult = MaaFramework.resource().MaaResourceGetNodeList(
                    resource.getHandle(),
                    stringBuffer.getHandle()
            ).getValue();

            log.info("get task list result: {}", getResult);

            List<String> value = stringBuffer.getValue();
//            log.info("get task list value: {}", value);
            value.forEach(item -> log.info("item: {}", item));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void setGpu() {
        boolean b = resource.setGpu(0);
        Assertions.assertTrue(b, "Failed to set gpu.");
    }

    @Test
    void setCpu() {
        boolean b = resource.setCpu();
        Assertions.assertTrue(b, "Failed to set cpu.");
    }

    @Test
    void setAutoDevice() {
        boolean b = resource.setAutoDevice();
        Assertions.assertTrue(b, "Failed to set auto device.");
    }

    @Test
    void useCpu() {
        boolean b = resource.useCpu();
        Assertions.assertTrue(b, "Failed useCpu.");
    }

    @Test
    void useDirectMl() {
        boolean b = resource.useDirectMl();
        Assertions.assertTrue(b, "Failed useDirectMl.");
    }

    @Test
    void useCoreMl() {
        boolean b = resource.useCoreMl();
        Assertions.assertTrue(b, "Failed useCoreMl.");
    }

    @Test
    void useAutoEp() {
        boolean b = resource.useAutoEp();
        Assertions.assertTrue(b, "Failed useAutoEp.");
    }


}