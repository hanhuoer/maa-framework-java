package io.github.hanhuoer.maa.core.base;

import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.define.StringListBuffer;
import io.github.hanhuoer.maa.jna.MaaFramework;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void close() {
    }

    @Test
    void load() {
        Boolean load = resource.load("./resources/resource");
        log.info("load result: {}", load);
    }

    @Test
    void postPath() {
    }

    @Test
    void loaded() {
        log.info("loaded: {}", resource.loaded());

        Boolean load = resource.load("./resources/resource");
        log.info("load result: {}", load);

        log.info("loaded: {}", resource.loaded());
    }

    @Test
    void clear() {
    }

    @Test
    void status() {
    }


    @Test
    void loadAndGetTest() {
        Boolean load = resource.load("./resources/resource");

        try (StringListBuffer stringBuffer = new StringListBuffer()) {
            Boolean getResult = MaaFramework.resource().MaaResourceGetTaskList(
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

}