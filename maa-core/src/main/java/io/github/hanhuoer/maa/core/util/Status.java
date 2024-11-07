package io.github.hanhuoer.maa.core.util;

import io.github.hanhuoer.maa.consts.MaaStatusEnum;
import io.github.hanhuoer.maa.define.MaaStatus;

/**
 * @author H
 */
public class Status {
    private final MaaStatusEnum status;

    public Status(MaaStatusEnum status) {
        this.status = status;
    }

    public Status(MaaStatus status) {
        this.status = MaaStatusEnum.of(status);
    }

    public Status(MaaStatus.Reference status) {
        this.status = MaaStatusEnum.of(status.getValue().longValue());
    }

    public Status(Integer status) {
        this.status = MaaStatusEnum.of(status);
    }

    public Status(Long status) {
        this.status = MaaStatusEnum.of(status);
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