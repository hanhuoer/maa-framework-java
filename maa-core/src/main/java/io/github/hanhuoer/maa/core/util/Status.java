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
        return status == MaaStatusEnum.SUCCESS || status == MaaStatusEnum.FAILURE;
    }

    public boolean success() {
        return status == MaaStatusEnum.SUCCESS;
    }

    public boolean failure() {
        return status == MaaStatusEnum.FAILURE;
    }

    public boolean pending() {
        return status == MaaStatusEnum.PENDING;
    }

    public boolean running() {
        return status == MaaStatusEnum.RUNNING;
    }
}