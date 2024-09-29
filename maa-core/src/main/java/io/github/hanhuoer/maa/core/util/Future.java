package io.github.hanhuoer.maa.core.util;

import io.github.hanhuoer.maa.consts.MaaStatusEnum;
import io.github.hanhuoer.maa.define.MaaId;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author H
 */
@Getter
@Slf4j
public class Future {

    private final MaaId maaid;
    private final Function<MaaId, MaaStatusEnum> statusFunc;
    private final Function<MaaId, Void> waitFunc;


    public Future(MaaId maaid, Function<MaaId, MaaStatusEnum> statusFunc, Function<MaaId, Void> waitFunc) {
        this.maaid = maaid;
        this.statusFunc = statusFunc;
        this.waitFunc = waitFunc;
    }

    public long getId() {
        return maaid.getValue();
    }

    @SneakyThrows
    public Future waiting() {
        waitFunc.apply(maaid);

//        while (!status().done()) {
//            Thread.sleep(0);
//        }

        return this;
    }

    public Status status() {
        return new Status(statusFunc.apply(maaid));
    }

    public boolean done() {
        return status().done();
    }

    public boolean succeeded() {
        return status().succeeded();
    }

    public boolean failed() {
        return status().failed();
    }

    public boolean pending() {
        return status().pending();
    }

    public boolean running() {
        return status().running();
    }


}



