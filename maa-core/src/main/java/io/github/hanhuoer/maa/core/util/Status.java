package io.github.hanhuoer.maa.core.util;

import io.github.hanhuoer.maa.consts.MaaStatusEnum;

/**
 * @author H
 */
public class Status {
    private final MaaStatusEnum status;

    public Status(MaaStatusEnum status) {
        this.status = status;
    }

    public boolean done() {
        return status == MaaStatusEnum.SUCCEEDED || status == MaaStatusEnum.FAILED;
    }

    public boolean succeeded() {
        return status == MaaStatusEnum.SUCCEEDED;
    }

    public boolean failed() {
        return status == MaaStatusEnum.FAILED;
    }

    public boolean pending() {
        return status == MaaStatusEnum.PENDING;
    }

    public boolean running() {
        return status == MaaStatusEnum.RUNNING;
    }
}