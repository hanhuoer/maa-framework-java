package io.github.hanhuoer.maa.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import io.github.hanhuoer.maa.MaaOptions;
import io.github.hanhuoer.maa.callbak.MaaNotificationCallback;
import io.github.hanhuoer.maa.loader.MaaLibraryLoader;
import io.github.hanhuoer.maa.ptr.*;
import io.github.hanhuoer.maa.ptr.base.MaaBool;
import io.github.hanhuoer.maa.ptr.base.MaaLong;
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
    protected MaaTasker tasker;
    protected MaaContext context;

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

    public static MaaTasker tasker() {
        checkMaaFramework();
        return INSTANCE.tasker;
    }

    public static MaaContext context() {
        checkMaaFramework();
        return INSTANCE.context;
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
        if (INSTANCE.tasker == null) {
            throw new RuntimeException("MaaFramework.tasker can not be null.");
        }
        if (INSTANCE.context == null) {
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
        tasker = Native.load(libPath, MaaTasker.class);
        context = Native.load(libPath, MaaContext.class);
    }

    /**
     * MaaController.h File Reference
     *
     * @link <a href="https://maaxyz.github.io/MaaFramework/MaaController_8h.html">...</a>
     */
    public interface MaaController extends Library {

        MaaControllerHandle MaaAdbControllerCreate(String adb_path, String address,
                                                   MaaAdbScreencapMethod screencap_methods,
                                                   MaaAdbInputMethod input_methods,
                                                   String config,
                                                   String agent_path,
                                                   MaaNotificationCallback callback, MaaCallbackTransparentArg callback_arg);

        MaaControllerHandle MaaWin32ControllerCreate(MaaToolkitDesktopWindowHandle hWnd,
                                                     MaaWin32ScreencapMethod screencap_method,
                                                     MaaWin32InputMethod input_method,
                                                     MaaNotificationCallback callback, MaaCallbackTransparentArg callback_arg);

        MaaControllerHandle MaaCustomControllerCreate(MaaCustomControllerHandle handle, MaaTransparentArg handle_arg,
                                                      MaaNotificationCallback callback, MaaCallbackTransparentArg callback_arg);

        MaaControllerHandle MaaDbgControllerCreate(String read_path, String write_path,
                                                   MaaDbgControllerType type, String config,
                                                   MaaNotificationCallback callback, MaaCallbackTransparentArg callback_arg);

        void MaaControllerDestroy(MaaControllerHandle controllerHandle);

        /**
         * @param value byte array, int*, char*, bool*
         */
        MaaBool MaaControllerSetOption(MaaControllerHandle ctrl, MaaCtrlOption key,
                                       MaaOptionValue value, MaaOptionValueSize value_size);

        MaaCtrlId MaaControllerPostConnection(MaaControllerHandle ctrl);

        MaaCtrlId MaaControllerPostClick(MaaControllerHandle ctrl, int x, int y);

        MaaCtrlId MaaControllerPostSwipe(MaaControllerHandle ctrl, int x1, int y1, int x2, int y2, int duration);

        MaaCtrlId MaaControllerPostPressKey(MaaControllerHandle ctrl, int keycode);

        MaaCtrlId MaaControllerPostInputText(MaaControllerHandle ctrl, String text);

        MaaCtrlId MaaControllerPostStartApp(MaaControllerHandle ctrl, String intent);

        MaaCtrlId MaaControllerPostStopApp(MaaControllerHandle ctrl, String intent);

        MaaCtrlId MaaControllerPostTouchDown(MaaControllerHandle ctrl, int contact, int x, int y, int pressure);

        MaaCtrlId MaaControllerPostTouchMove(MaaControllerHandle ctrl, int contact, int x, int y, int pressure);

        MaaCtrlId MaaControllerPostTouchUp(MaaControllerHandle ctrl, int contact);

        MaaCtrlId MaaControllerPostScreencap(MaaControllerHandle ctrl);

        MaaStatus MaaControllerStatus(MaaControllerHandle ctrl, MaaCtrlId id);

        MaaStatus MaaControllerWait(MaaControllerHandle ctrl, MaaCtrlId id);

        MaaBool MaaControllerConnected(MaaControllerHandle ctrl);

        /**
         * @param buffer out
         */
        MaaBool MaaControllerCachedImage(MaaControllerHandle ctrl, MaaImageBufferHandle buffer);

        /**
         * @param buffer out
         */
        MaaBool MaaControllerGetUUID(MaaControllerHandle ctrl, MaaStringBufferHandle buffer);

    }

    /**
     * MaaBuffer.h File Reference
     *
     * @link <a href="https://maaxyz.github.io/MaaFramework/MaaBuffer_8h.html">...</a>
     */
    public interface MaaBuffer extends Library {

        MaaStringBufferHandle MaaStringBufferCreate();

        void MaaStringBufferDestroy(MaaStringBufferHandle handle);

        MaaBool MaaStringBufferIsEmpty(MaaStringBufferHandle handle);

        MaaBool MaaStringBufferClear(MaaStringBufferHandle handle);

        String MaaStringBufferGet(MaaStringBufferHandle handle);

        MaaSize MaaStringBufferSize(MaaStringBufferHandle handle);

        MaaBool MaaStringBufferSet(MaaStringBufferHandle handle, String str);

        MaaBool MaaStringBufferSetEx(MaaStringBufferHandle handle, String str, MaaSize size);

        MaaStringListBufferHandle MaaStringListBufferCreate();

        void MaaStringListBufferDestroy(MaaStringListBufferHandle handle);

        MaaBool MaaStringListBufferIsEmpty(MaaStringListBufferHandle handle);

        MaaSize MaaStringListBufferSize(MaaStringListBufferHandle handle);

        MaaStringBufferHandle MaaStringListBufferAt(MaaStringListBufferHandle handle, MaaSize index);

        MaaBool MaaStringListBufferAppend(MaaStringListBufferHandle handle, MaaStringBufferHandle value);

        MaaBool MaaStringListBufferRemove(MaaStringListBufferHandle handle, MaaSize index);

        MaaBool MaaStringListBufferClear(MaaStringListBufferHandle handle);

        MaaImageBufferHandle MaaImageBufferCreate();

        void MaaImageBufferDestroy(MaaImageBufferHandle handle);

        MaaBool MaaImageBufferIsEmpty(MaaImageBufferHandle handle);

        MaaBool MaaImageBufferClear(MaaImageBufferHandle handle);

        MaaImageRawData MaaImageBufferGetRawData(MaaImageBufferHandle handle);

        Integer MaaImageBufferWidth(MaaImageBufferHandle handle);

        Integer MaaImageBufferHeight(MaaImageBufferHandle handle);

        Integer MaaImageBufferChannels(MaaImageBufferHandle handle);

        Integer MaaImageBufferType(MaaImageBufferHandle handle);

        MaaBool MaaImageBufferSetRawData(MaaImageBufferHandle handle, MaaImageRawData data, int width, int height, int type);

        MaaImageEncodedData MaaImageBufferGetEncoded(MaaImageBufferHandle handle);

        MaaSize MaaImageBufferGetEncodedSize(MaaImageBufferHandle handle);

        MaaBool MaaImageBufferSetEncoded(MaaImageBufferHandle handle, MaaImageEncodedData data, MaaSize size);

        MaaImageListBufferHandle MaaImageListBufferCreate();

        void MaaImageListBufferDestroy(MaaImageListBufferHandle handle);

        MaaBool MaaImageListBufferIsEmpty(MaaImageListBufferHandle handle);

        MaaSize MaaImageListBufferSize(MaaImageListBufferHandle handle);

        MaaImageBufferHandle MaaImageListBufferAt(MaaImageListBufferHandle handle, MaaSize size);

        MaaBool MaaImageListBufferAppend(MaaImageListBufferHandle handle, MaaImageBufferHandle value);

        MaaBool MaaImageListBufferRemove(MaaImageListBufferHandle handle, MaaSize size);

        MaaBool MaaImageListBufferClear(MaaImageListBufferHandle handle);

        MaaRectHandle MaaRectCreate();

        void MaaRectDestroy(MaaRectHandle handle);

        Integer MaaRectGetX(MaaRectHandle handle);

        Integer MaaRectGetY(MaaRectHandle handle);

        Integer MaaRectGetW(MaaRectHandle handle);

        Integer MaaRectGetH(MaaRectHandle handle);

        MaaBool MaaRectSet(MaaRectHandle handle, int x, int y, int w, int h);

    }


    /**
     * MaaUtility.h File Reference
     *
     * <a href=" @link https://maaxyz.github.io/MaaFramework/MaaUtility_8h.html">...</a>
     */
    public interface MaaUtility extends Library {

        String MaaVersion();

        MaaBool MaaSetGlobalOption(MaaGlobalOption key, MaaOptionValue value, MaaOptionValueSize val_size);

    }


    /**
     * MaaResource.h File Reference
     *
     * <a href=" @link https://maaxyz.github.io/MaaFramework/MaaResource_8h.html">...</a>
     */
    public interface MaaResource extends Library {

        MaaResourceHandle MaaResourceCreate(MaaNotificationCallback callback, MaaCallbackTransparentArg callback_arg);

        void MaaResourceDestroy(MaaResourceHandle res);

        MaaBool MaaResourceRegisterCustomRecognition(MaaResourceHandle res, String name, MaaCustomRecognitionHandle recognizer, MaaTransparentArg trans_arg);

        MaaBool MaaResourceUnregisterCustomRecognition(MaaResourceHandle res, String name);

        MaaBool MaaResourceClearCustomRecognition(MaaResourceHandle res);

        MaaBool
        MaaResourceRegisterCustomAction(MaaResourceHandle res, String name, MaaCustomActionHandle action, MaaTransparentArg trans_arg);

        MaaBool MaaResourceUnregisterCustomAction(MaaResourceHandle res, String name);

        MaaBool MaaResourceClearCustomAction(MaaResourceHandle res);

        MaaResId MaaResourcePostPath(MaaResourceHandle res, String path);

        MaaBool MaaResourceClear(MaaResourceHandle res);

        /**
         * @param id resource id
         */
        MaaStatus MaaResourceStatus(MaaResourceHandle res, MaaResId id);

        /**
         * @param id resource id
         */
        MaaStatus MaaResourceWait(MaaResourceHandle res, MaaResId id);

        MaaBool MaaResourceLoaded(MaaResourceHandle res);

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
        MaaBool MaaResourceSetOption(MaaResourceHandle res, MaaResOption key, MaaOptionValue value, MaaOptionValueSize val_size);

        /**
         * Get the hash of the resource.
         *
         * @param buffer out, the buffer where the hash will be written to.
         */
        MaaBool MaaResourceGetHash(MaaResourceHandle res, MaaStringBufferHandle buffer);

        /**
         * Get the task list of the resource.
         *
         * @param buffer out, the buffer where the task list will be written to.
         */
        MaaBool MaaResourceGetTaskList(MaaResourceHandle res, MaaStringListBufferHandle buffer);

    }


    /**
     * MaaTasker.h File Reference
     *
     * @link <a href="https://maaxyz.github.io/MaaFramework/MaaTasker_8h.html">...</a>
     */
    public interface MaaTasker extends Library {

        MaaTaskerHandle MaaTaskerCreate(MaaNotificationCallback callback, MaaCallbackTransparentArg callback_arg);

        void MaaTaskerDestroy(MaaTaskerHandle tasker);

        /**
         * @param value in, byte array, int*, char*, bool*
         */
        MaaBool MaaTaskerSetOption(MaaTaskerHandle tasker, MaaTaskerOption key,
                                   MaaOptionValue value, MaaOptionValueSize val_size);

        MaaBool MaaTaskerBindResource(MaaTaskerHandle tasker, MaaResourceHandle res);

        MaaBool MaaTaskerBindController(MaaTaskerHandle tasker, MaaControllerHandle ctrl);

        MaaBool MaaTaskerInited(MaaTaskerHandle tasker);

        MaaTaskId MaaTaskerPostPipeline(MaaTaskerHandle tasker, String entry, String pipeline_override);

//        MaaTaskId MaaTaskerPostRecognition(MaaTaskerHandle tasker, String entry, String pipeline_override);

//        MaaTaskId MaaTaskerPostAction(MaaTaskerHandle tasker, String entry, String pipeline_override);

        MaaStatus MaaTaskerStatus(MaaTaskerHandle tasker, MaaTaskId id);

        MaaStatus MaaTaskerWait(MaaTaskerHandle tasker, MaaTaskId id);

        MaaBool MaaTaskerRunning(MaaTaskerHandle tasker);

        MaaBool MaaTaskerPostStop(MaaTaskerHandle tasker);

        MaaResourceHandle MaaTaskerGetResource(MaaTaskerHandle tasker);

        MaaControllerHandle MaaTaskerGetController(MaaTaskerHandle tasker);

        MaaBool MaaTaskerClearCache(MaaTaskerHandle tasker);

        /**
         * @param name        out
         * @param algorithm   out
         * @param hit         out
         * @param box         out
         * @param detail_json out
         * @param raw         out, only valid in debug mode
         * @param draws       out, only valid in debug mode
         */
        MaaBool MaaTaskerGetRecognitionDetail(MaaTaskerHandle tasker,
                                              MaaRecoId reco_id,
                                              MaaStringBufferHandle name,
                                              MaaStringBufferHandle algorithm,
                                              MaaBool hit,
                                              MaaRectHandle box,
                                              MaaStringBufferHandle detail_json,
                                              MaaImageBufferHandle raw,
                                              MaaImageListBufferHandle draws);

        /**
         * @param name      out
         * @param reco_id   out
         * @param completed out
         */
        MaaBool MaaTaskerGetNodeDetail(MaaTaskerHandle tasker,
                                       MaaNodeId node_id,
                                       MaaStringBufferHandle name,
                                       MaaRecoId.Reference reco_id,
                                       MaaBool completed);

        /**
         * @param node_id_list      out, array
         * @param node_id_list_size in, out
         */
        MaaBool MaaTaskerGetTaskDetail(MaaTaskerHandle tasker,
                                       MaaTaskId task_id,
                                       MaaStringBufferHandle entry,
                                       MaaNodeIdArr node_id_list,
                                       MaaSize.Reference node_id_list_size);

        /**
         * @param[out] latest_id
         */
        MaaBool MaaTaskerGetLatestNode(MaaTaskerHandle tasker, String task_name,/* out */ MaaLong.Reference latest_id);

    }


    /**
     * MaaContext.h File Reference
     *
     * @link <a href="https://maaxyz.github.io/MaaFramework/MaaContext_8h.html">...</a>
     */
    public interface MaaContext extends Library {

        MaaTaskId MaaContextRunPipeline(MaaContextHandle context, String entry, String pipeline_override);

        MaaRecoId MaaContextRunRecognition(MaaContextHandle context, String entry, String pipeline_override, MaaImageBufferHandle image);

        MaaNodeId MaaContextRunAction(MaaContextHandle context, String entry, String pipeline_override,
                                      MaaRectHandle box, String reco_detail);

        MaaBool MaaContextOverridePipeline(MaaContextHandle context, String pipeline_override);

        MaaBool MaaContextOverrideNext(MaaContextHandle context, String name, MaaStringListBufferHandle next_list);

        MaaTaskId MaaContextGetTaskId(MaaContextHandle context);

        MaaTaskerHandle MaaContextGetTasker(MaaContextHandle context);

        MaaContextHandle MaaContextClone(MaaContextHandle context);

    }

}
