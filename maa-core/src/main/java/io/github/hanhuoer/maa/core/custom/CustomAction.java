package io.github.hanhuoer.maa.core.custom;

import io.github.hanhuoer.maa.model.Rect;
import io.github.hanhuoer.maa.ptr.*;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author H
 */
@Getter
@Accessors(chain = true)
public abstract class CustomAction {

    private final MaaCustomActionHandle handle;


    public CustomAction() {
        this.handle = new MaaCustomActionHandle();
        handle.action = this::runAgent;
        handle.stop = this::stopAgent;
    }

    /**
     * run the given action.
     *
     * @param context     the context.
     * @param taskName    the name of the task.
     * @param customParam the custom action param from pipeline.
     * @param box         the current box.
     * @param recDetail   the current recognition detail.
     * @return success or not.
     */
    public abstract boolean run(
            SyncContext context,
            String taskName,
            String customParam,
            Rect box,
            String recDetail
    );

    /**
     * stop the given action.
     */
    public abstract void stop();

    public boolean runAgent(
            MaaSyncContextHandle contextHandle,
            String taskName,
            String customParam,
            MaaRectHandle boxHandle,
            String recDetail,
            MaaTransparentArg arg
    ) {
        SyncContext syncContext = new SyncContext(contextHandle);

        RectBuffer rectBuffer = new RectBuffer(boxHandle);
        Rect box = rectBuffer.getValue();
        rectBuffer.close();

        return this.run(
                syncContext,
                taskName,
                customParam,
                box,
                recDetail
        );
    }

    public void stopAgent() {
        this.stop();
    }
}
