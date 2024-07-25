package io.github.hanhuoer.maa.core.base;

import io.github.hanhuoer.maa.MaaWindowsAarch64LibraryLoader;
import io.github.hanhuoer.maa.loader.LibraryLoader;
import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.callbak.MaaResourceCallback;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.ptr.StringBuffer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

@Slf4j
class ResourceTest {

    private Resource resource;
    private Maa maa;


    @BeforeAll
    static void init() throws Exception {
        LibraryLoader loader = new MaaWindowsAarch64LibraryLoader();
        loader.loadLibrary();
    }

    @BeforeEach
    void setUp() {
        maa = Maa.create();

        MaaResourceCallback callback = (msg, detailsJson, callbackArg) -> {
            log.info("received callback: ********************************************");
            log.info("message: {}", msg);
            log.info("detailJson: {}", detailsJson);
            log.info("callbackArg: {}", callbackArg);
        };

        resource = new Resource(callback, null);
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
        CompletableFuture<Boolean> load = resource.load("./resources/resource");
        Boolean join = load.join();
        log.info("load result: {}", join);
    }

    @Test
    void postPath() {
    }

    @Test
    void loaded() {
        log.info("loaded: {}", resource.loaded());

        CompletableFuture<Boolean> load = resource.load("./resources/resource");
        Boolean join = load.join();
        log.info("load result: {}", join);

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
        CompletableFuture<Boolean> load = resource.load("./resources/resource");
        Boolean join = load.join();

        try (StringBuffer stringBuffer = new StringBuffer()) {
            Boolean getResult = MaaFramework.resource().MaaResourceGetTaskList(
                    resource.getHandle(),
                    stringBuffer.getHandle()
            );

            log.info("get task list result: {}", getResult);

            String value = stringBuffer.getValue();
            log.info("get task list value: {}", value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}