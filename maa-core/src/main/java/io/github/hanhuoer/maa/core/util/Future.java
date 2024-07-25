package io.github.hanhuoer.maa.core.util;

import io.github.hanhuoer.maa.consts.MaaStatusEnum;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * @author H
 */
@Getter
public abstract class Future {

    private final Long maaid;
    private final Function<Long, MaaStatusEnum> statusFunc;

    public Future(Long maaid, Function<Long, MaaStatusEnum> statusFunc) {
        this.maaid = maaid;
        this.statusFunc = statusFunc;
    }

    public CompletableFuture<Boolean> waitForCompletion() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                while (!status().done()) {
                    Thread.sleep(0);
                }
                return success();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        });
    }

    public Status status() {
        return new Status(statusFunc.apply(maaid));
    }

    public boolean done() {
        return status().done();
    }

    public boolean success() {
        return status().success();
    }

    public boolean failure() {
        return status().failure();
    }

    public boolean pending() {
        return status().pending();
    }

    public boolean running() {
        return status().running();
    }


}



