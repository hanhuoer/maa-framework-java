package io.github.hanhuoer.maa.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import io.github.hanhuoer.maa.loader.MaaLibraryLoader;
import io.github.hanhuoer.maa.MaaOptions;
import io.github.hanhuoer.maa.callbak.MaaControllerCallback;
import io.github.hanhuoer.maa.callbak.MaaInstanceCallback;
import io.github.hanhuoer.maa.callbak.MaaResourceCallback;
import io.github.hanhuoer.maa.ptr.*;
import io.github.hanhuoer.maa.util.FileUtils;
import lombok.Getter;

/**
 * @author H
 */
@Getter
public class MaaFramework {

    private static volatile MaaFramework INSTANCE;
    protected MaaController controller;
    protected MaaBuffer buffer;
    protected MaaUtility utility;
    protected MaaResource resource;
    protected MaaInstance instance;
    protected MaaSyncContext syncContext;

    private MaaFramework() {
    }

    public static MaaFramework create(MaaLibraryLoader nativeLoader, MaaOptions options) {
        if (INSTANCE == null) {
            synchronized (MaaFramework.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MaaFramework();

                    INSTANCE.init(nativeLoader, options);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroy() {

    }

    public static MaaController controller() {
        checkMaaFramework();
        return INSTANCE.controller;
    }

    public static MaaBuffer buffer() {
        checkMaaFramework();
        return INSTANCE.buffer;
    }

    public static MaaUtility utility() {
        checkMaaFramework();
        return INSTANCE.utility;
    }

    public static MaaResource resource() {
        checkMaaFramework();
        return INSTANCE.resource;
    }

    public static MaaInstance instance() {
        checkMaaFramework();
        return INSTANCE.instance;
    }

    public static MaaSyncContext syncContext() {
        checkMaaFramework();
        return INSTANCE.syncContext;
    }

    public static void checkMaaFramework() {
        if (INSTANCE == null) {
            throw new RuntimeException("MaaFramework instance can not be null.");
        }
        if (INSTANCE.controller == null) {
            throw new RuntimeException("MaaFramework.controller can not be null.");
        }
        if (INSTANCE.buffer == null) {
            throw new RuntimeException("MaaFramework.buffer can not be null.");
        }
        if (INSTANCE.utility == null) {
            throw new RuntimeException("MaaFramework.utility can not be null.");
        }
        if (INSTANCE.resource == null) {
            throw new RuntimeException("MaaFramework.resource can not be null.");
        }
        if (INSTANCE.instance == null) {
            throw new RuntimeException("MaaFramework.instance can not be null.");
        }
        if (INSTANCE.syncContext == null) {
            throw new RuntimeException("MaaFramework.syncContext can not be null.");
        }
    }

    /**
     * 初始化
     * 创建本地接口
     *
     * @param nativeLoader native loader
     * @param options      maa options
     */
    private void init(MaaLibraryLoader nativeLoader, MaaOptions options) {
        String libPath = FileUtils.join(options.getLibDir(), nativeLoader.getMaaFrameworkName()).getAbsolutePath();
        controller = Native.load(libPath, MaaController.class);
        buffer = Native.load(libPath, MaaBuffer.class);
        utility = Native.load(libPath, MaaUtility.class);
        resource = Native.load(libPath, MaaResource.class);
        instance = Native.load(libPath, MaaInstance.class);
        syncContext = Native.load(libPath, MaaSyncContext.class);
    }

    /**
     * MaaController.h File Reference
     *
     * @link <a href="https://maaxyz.github.io/MaaFramework/MaaController_8h.html">...</a>
     */
    public interface MaaController extends Library {

        /**
         * @deprecated use {@link #MaaAdbControllerCreateV2(String, String, int, String, String, MaaControllerCallback, MaaCallbackTransparentArg)} instead.
         */
        MaaControllerHandle MaaAdbControllerCreate(String adb_path, String address, int type, String config, MaaControllerCallback callback, MaaCallbackTransparentArg callback_arg);

        MaaControllerHandle MaaAdbControllerCreateV2(String adb_path, String address, int type, String config, String agent_path, MaaControllerCallback callback, MaaCallbackTransparentArg callback_arg);

        /**
         * @param hWnd         MaaWin32Hwnd
         * @param type         MaaWin32ControllerType
         * @param callback     MaaControllerCallback
         * @param callback_arg MaaCallbackTransparentArg
         * @return MaaControllerHandle
         */
        MaaControllerHandle MaaWin32ControllerCreate(MaaWin32Hwnd hWnd, int type, MaaControllerCallback callback, MaaCallbackTransparentArg callback_arg);

        /**
         * @param handle MaaCustomControllerHandle
         */
        MaaControllerHandle MaaCustomControllerCreate(MaaCustomControllerHandle handle, MaaTransparentArg handle_arg, MaaControllerCallback callback, MaaCallbackTransparentArg callback_arg);

        /**
         * @param type MaaThriftControllerType
         */
        MaaControllerHandle MaaThriftControllerCreate(MaaThriftControllerType type, String host, int port, String config, MaaControllerCallback callback, MaaCallbackTransparentArg callback_arg);

        /**
         * @param type MaaDbgControllerType
         */
        MaaControllerHandle MaaDbgControllerCreate(String read_path, String write_path, int type, String config, MaaControllerCallback callback, MaaCallbackTransparentArg callback_arg);

        void MaaControllerDestroy(MaaControllerHandle controllerHandle);

        Long MaaControllerSetOption(MaaControllerHandle ctrl, int key, int value, int value_size);

        /**
         * @param ctrl MaaControllerHandle
         * @return typedef int64_t MaaId
         */
        Long MaaControllerPostConnection(MaaControllerHandle ctrl);

        Long MaaControllerPostClick(MaaControllerHandle ctrl, int x, int y);

        Long MaaControllerPostSwipe(MaaControllerHandle ctrl, int x1, int y1, int x2, int y2, int duration);

        Long MaaControllerPostPressKey(MaaControllerHandle ctrl, int keycode);

        Long MaaControllerPostInputText(MaaControllerHandle ctrl, String text);

        Long MaaControllerPostStartApp(MaaControllerHandle ctrl, String intent);

        Long MaaControllerPostStopApp(MaaControllerHandle ctrl, String intent);

        Long MaaControllerPostTouchDown(MaaControllerHandle ctrl, int contact, int x, int y, int pressure);

        Long MaaControllerPostTouchMove(MaaControllerHandle ctrl, int contact, int x, int y, int pressure);

        Long MaaControllerPostTouchUp(MaaControllerHandle ctrl, int contact);

        Long MaaControllerPostScreencap(MaaControllerHandle ctrl);

        /**
         * @param ctrl    MaaControllerHandle
         * @param ctrl_id MaaCtrlId id
         * @return typedef int32_t MaaStatus
         */
        Integer MaaControllerStatus(MaaControllerHandle ctrl, long ctrl_id);

        /**
         * @param controller_handle MaaControllerHandle
         * @param ctrl_id           MaaCtrlId id
         * @return typedef int32_t MaaStatus
         */
        Integer MaaControllerWait(MaaControllerHandle controller_handle, long ctrl_id);

        Boolean MaaControllerConnected(MaaControllerHandle ctrl);

        Boolean MaaControllerGetImage(MaaControllerHandle ctrl, MaaImageBufferHandle buffer);

        Boolean MaaControllerGetUUID(MaaControllerHandle ctrl, MaaStringBufferHandle buffer);

    }

    /**
     * MaaBuffer.h File Reference
     *
     * @link <a href="https://maaxyz.github.io/MaaFramework/MaaBuffer_8h.html">...</a>
     */
    public interface MaaBuffer extends Library {

        MaaStringBufferHandle MaaCreateStringBuffer();

        void MaaDestroyStringBuffer(MaaStringBufferHandle handle);

        Boolean MaaIsStringEmpty(MaaStringBufferHandle handle);

        Boolean MaaClearString(MaaStringBufferHandle handle);

        String MaaGetString(MaaStringBufferHandle handle);

        Long MaaGetStringSize(MaaStringBufferHandle handle);

        Boolean MaaSetString(MaaStringBufferHandle handle, String str);

        Boolean MaaSetStringEx(MaaStringBufferHandle handle, String str, long size);

        MaaStringListBufferHandle MaaCreateStringListBuffer();

        void MaaDestroyStringListBuffer(MaaStringListBufferHandle handle);

        Boolean MaaIsStringListEmpty(MaaStringListBufferHandle handle);

        Boolean MaaClearStringList(MaaStringListBufferHandle handle);

        Long MaaGetStringListSize(MaaStringListBufferHandle handle);

        MaaStringBufferHandle MaaGetStringListAt(MaaStringListBufferHandle handle, long index);

        Boolean MaaStringListAppend(MaaStringListBufferHandle handle, MaaStringBufferHandle value);

        Boolean MaaStringListRemove(MaaStringListBufferHandle handle, long index);

        MaaImageBufferHandle MaaCreateImageBuffer();

        void MaaDestroyImageBuffer(MaaImageBufferHandle handle);

        Boolean MaaIsImageEmpty(MaaImageBufferHandle handle);

        Boolean MaaClearImage(MaaImageBufferHandle handle);

        MaaImageRawData MaaGetImageRawData(MaaImageBufferHandle handle);

        Integer MaaGetImageWidth(MaaImageBufferHandle handle);

        Integer MaaGetImageHeight(MaaImageBufferHandle handle);

        Integer MaaGetImageType(MaaImageBufferHandle handle);

        Boolean MaaSetImageRawData(MaaImageBufferHandle handle, MaaImageRawData data, int width, int height, int type);

        MaaImageEncodedData MaaGetImageEncoded(MaaImageBufferHandle handle);

        Long MaaGetImageEncodedSize(MaaImageBufferHandle handle);

        Boolean MaaSetImageEncoded(MaaImageBufferHandle handle, MaaImageRawData data, long size);

        MaaImageListBufferHandle MaaCreateImageListBuffer();

        void MaaDestroyImageListBuffer(MaaImageListBufferHandle handle);

        Boolean MaaIsImageListEmpty(MaaImageListBufferHandle handle);

        Boolean MaaClearImageList(MaaImageListBufferHandle handle);

        Long MaaGetImageListSize(MaaImageListBufferHandle handle);

        MaaImageBufferHandle MaaGetImageListAt(MaaImageListBufferHandle handle, long size);

        Boolean MaaImageListAppend(MaaImageListBufferHandle handle, MaaImageBufferHandle value);

        Boolean MaaImageListRemove(MaaImageListBufferHandle handle, long size);

        MaaRectHandle MaaCreateRectBuffer();

        void MaaDestroyRectBuffer(MaaRectHandle handle);

        Integer MaaGetRectX(MaaRectHandle handle);

        Integer MaaGetRectY(MaaRectHandle handle);

        Integer MaaGetRectW(MaaRectHandle handle);

        Integer MaaGetRectH(MaaRectHandle handle);

        Boolean MaaSetRect(MaaRectHandle handle, int x, int y, int w, int h);

        Boolean MaaSetRectX(MaaRectHandle handle, int value);

        Boolean MaaSetRectY(MaaRectHandle handle, int value);

        Boolean MaaSetRectW(MaaRectHandle handle, int value);

        Boolean MaaSetRectH(MaaRectHandle handle, int value);

    }


    /**
     * MaaUtility.h File Reference
     *
     * <a href=" @link https://maaxyz.github.io/MaaFramework/MaaUtility_8h.html">...</a>
     */
    public interface MaaUtility extends Library {

        String MaaVersion();

        /**
         * @param key      MaaGlobalOption
         * @param value    MaaOptionValue
         * @param val_size MaaOptionValueSize
         * @return MaaBool
         */
        Boolean MaaSetGlobalOption(int key, MaaOptionValue value, long val_size);

        Boolean MaaQueryRecognitionDetail(long reco_id,
                                          MaaStringBufferHandle name,
                                          MaaBool hit,
                                          MaaRectHandle hit_box,
                                          MaaStringBufferHandle detail_json,
                                          MaaImageBufferHandle raw,
                                          MaaImageListBufferHandle draws);

        Boolean MaaQueryNodeDetail(long node_id, MaaStringBufferHandle name, MaaRecoId reco_id, MaaBool run_completed);

        /**
         * @param node_id_list      output
         * @param node_id_list_size input and output
         */
        Boolean MaaQueryTaskDetail(long task_id, MaaStringBufferHandle entry, MaaNodeIdArr node_id_list, MaaSize node_id_list_size);

    }


    /**
     * MaaResource.h File Reference
     *
     * <a href=" @link https://maaxyz.github.io/MaaFramework/MaaResource_8h.html">...</a>
     */
    public interface MaaResource extends Library {

        MaaResourceHandle MaaResourceCreate(MaaResourceCallback callback, MaaCallbackTransparentArg callback_arg);

        void MaaResourceDestroy(MaaResourceHandle res);

        /**
         * @param path resource path
         */
        Long MaaResourcePostPath(MaaResourceHandle res, String path);

        Boolean MaaResourceClear(MaaResourceHandle res);

        /**
         * @param id resource id
         */
        Integer MaaResourceStatus(MaaResourceHandle res, long id);

        /**
         * @param id resource id
         */
        Integer MaaResourceWait(MaaResourceHandle res, long id);

        Boolean MaaResourceLoaded(MaaResourceHandle res);

        /**
         * Set options for a given resource.
         * <p>
         * This function requires a given set of option keys and value types, otherwise this will fail.
         * See {@link io.github.hanhuoer.maa.consts.MaaResOptionEnum} for details.
         *
         * @param res      MaaResOption The handle of the resource to set options for.
         * @param key      MaaResOption The option key.
         * @param value    MaaOptionValue The option value. Maybe a byte array
         * @param val_size MaaOptionValueSize The size of the option value.
         * @return Whether the option is set successfully.
         */
        Boolean MaaResourceSetOption(MaaResourceHandle res, int key, MaaOptionValue value, long val_size);

        /**
         * Get the hash of the resource.
         *
         * @param buffer The buffer where the hash will be written to.
         */
        Boolean MaaResourceGetHash(MaaResourceHandle res, MaaStringBufferHandle buffer);

        /**
         * Get the task list of the resource.
         *
         * @param buffer The buffer where the task list will be written to.
         */
        Boolean MaaResourceGetTaskList(MaaResourceHandle res, MaaStringBufferHandle buffer);

    }


    /**
     * MaaInstance.h File Reference
     *
     * @link <a href="https://maaxyz.github.io/MaaFramework/MaaInstance_8h.html">...</a>
     */
    public interface MaaInstance extends Library {

        MaaInstanceHandle MaaCreate(MaaInstanceCallback maaAPICallback, MaaCallbackTransparentArg MaaTransparentArg);

        void MaaDestroy(MaaInstanceHandle controllerHandle);

        /**
         * @param key      MaaInstOption
         * @param value    MaaOptionValue
         * @param val_size MaaOptionValueSize
         */
        Boolean MaaSetOption(MaaInstanceHandle inst, int key, MaaOptionValue value, long val_size);

        Boolean MaaBindResource(MaaInstanceHandle inst, MaaResourceHandle res);

        Boolean MaaBindController(MaaInstanceHandle inst, MaaControllerHandle ctrl);

        Boolean MaaInited(MaaInstanceHandle inst);

        /**
         * @param recognizer MaaCustomRecognizerHandle
         */
        Boolean MaaRegisterCustomRecognizer(MaaInstanceHandle inst, String name, MaaCustomRecognizerHandle recognizer, MaaTransparentArg recognizer_arg);

        Boolean MaaUnregisterCustomRecognizer(MaaInstanceHandle inst, String name);

        Boolean MaaClearCustomRecognizer(MaaInstanceHandle inst);

        /**
         * @param action MaaCustomActionHandle
         */
        Boolean MaaRegisterCustomAction(MaaInstanceHandle inst, String name, MaaCustomActionHandle action, MaaTransparentArg action_arg);

        Boolean MaaUnregisterCustomAction(MaaInstanceHandle inst, String name);

        Boolean MaaClearCustomAction(MaaInstanceHandle inst);

        /**
         * @return task id
         */
        Long MaaPostTask(MaaInstanceHandle inst, String entry, String param);

        Long MaaPostRecognition(MaaInstanceHandle inst, String entry, String param);

        Long MaaPostAction(MaaInstanceHandle inst, String entry, String param);

        /**
         * @param id MaaTaskId
         */
        Boolean MaaSetTaskParam(MaaInstanceHandle inst, long id, String param);

        /**
         * @param id MaaTaskId
         * @return MaaStatus
         */
        Integer MaaTaskStatus(MaaInstanceHandle inst, long id);

        /**
         * @param taskId MaaTaskId
         */
        Integer MaaWaitTask(MaaInstanceHandle inst, long taskId);

        Boolean MaaTaskAllFinished(MaaInstanceHandle inst);

        Boolean MaaRunning(MaaInstanceHandle inst);

        Boolean MaaPostStop(MaaInstanceHandle inst);

        /**
         * @deprecated use  {@link #MaaPostStop(MaaInstanceHandle)}  instead.
         */
        Boolean MaaStop(MaaInstanceHandle inst);

        MaaResourceHandle MaaGetResource(MaaInstanceHandle inst);

        MaaControllerHandle MaaGetController(MaaInstanceHandle inst);

    }


    /**
     * MaaSyncContext.h File Reference
     *
     * @link <a href="https://maaxyz.github.io/MaaFramework/MaaSyncContext_8h.html">...</a>
     */
    public interface MaaSyncContext extends Library {

        MaaBool MaaSyncContextRunTask(MaaSyncContextHandle sync_context, String task_name, String param);

        MaaBool MaaSyncContextRunRecognition(MaaSyncContextHandle sync_context,
                                             MaaImageBufferHandle image,
                                             String task_name,
                                             String task_param,
                                             MaaRectHandle out_box /* out */,
                                             MaaStringBufferHandle out_detail /* out */);

        MaaBool MaaSyncContextRunAction(MaaSyncContextHandle sync_context,
                                        String task_name, String task_param,
                                        MaaRectHandle cur_box,
                                        String cur_rec_detail);

        MaaBool MaaSyncContextClick(MaaSyncContextHandle sync_context, int x, int y);

        MaaBool MaaSyncContextSwipe(MaaSyncContextHandle sync_context,
                                    int x1, int y1,
                                    int x2, int y2,
                                    int duration);

        MaaBool MaaSyncContextPressKey(MaaSyncContextHandle sync_context, int keycode);

        MaaBool MaaSyncContextInputText(MaaSyncContextHandle sync_context, String text);

        MaaBool MaaSyncContextTouchDown(MaaSyncContextHandle sync_context,
                                        int contact,
                                        int x,
                                        int y,
                                        int pressure);

        MaaBool MaaSyncContextTouchMove(MaaSyncContextHandle sync_context,
                                        int contact,
                                        int x,
                                        int y,
                                        int pressure);

        MaaBool MaaSyncContextTouchUp(MaaSyncContextHandle sync_context, int contact);

        MaaBool MaaSyncContextScreencap(MaaSyncContextHandle sync_context, MaaImageBufferHandle out_image);

        /**
         * @param sync_context sync context
         * @param out_image    out image
         */
        MaaBool MaaSyncContextCachedImage(MaaSyncContextHandle sync_context, MaaImageBufferHandle out_image);
    }

}
