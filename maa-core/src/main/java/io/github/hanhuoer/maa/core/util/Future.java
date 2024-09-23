package io.github.hanhuoer.maa.core.util;

import io.github.hanhuoer.maa.consts.MaaStatusEnum;
import io.github.hanhuoer.maa.ptr.MaaId;
import lombok.Getter;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author H
 */
@Getter
public class Future {

    private final MaaId maaid;
    private final Function<MaaId, MaaStatusEnum> statusFunc;
    private final Consumer<MaaId> waitFunc;


    public Future(MaaId maaid, Function<MaaId, MaaStatusEnum> statusFunc, Consumer<MaaId> waitFunc) {
        this.maaid = maaid;
        this.statusFunc = statusFunc;
        this.waitFunc = waitFunc;
    }

    public long getId() {
        return maaid.getValue();
    }

    public Future waiting() {
        waitFunc.accept(maaid);
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



