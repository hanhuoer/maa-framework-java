package io.github.hanhuoer.maa.callbak;

import com.sun.jna.Callback;
import com.sun.jna.ptr.IntByReference;
import io.github.hanhuoer.maa.ptr.*;

/**
 * @author H
 */
public interface MaaCustomControllerCallback {


    interface Connect extends Callback {

        Boolean callback(MaaTransparentArg handleArg);

    }

    interface RequestUUID extends Callback {

        /**
         * @param buffer output
         */
        Boolean callback(MaaTransparentArg handleArg, MaaStringBufferHandle buffer);

    }

    interface RequestResolution extends Callback {

        /**
         * Write result to width and height.
         *
         * @param width  output
         * @param height output
         */
        Boolean callback(MaaTransparentArg handleArg,
                         IntByReference width, IntByReference height);

    }

    interface StartApp extends Callback {

        Boolean callback(MaaStringView intent, MaaTransparentArg handleArg);

    }

    interface StopApp extends Callback {

        Boolean callback(MaaStringView intent, MaaTransparentArg handleArg);

    }

    interface Screencap extends Callback {

        /**
         * @param buffer output
         */
        Boolean callback(MaaTransparentArg handleArg, MaaImageBufferHandle buffer);

    }

    interface Click extends Callback {

        Boolean callback(IntByReference x, IntByReference y, MaaTransparentArg handleArg);

    }

    interface Swipe extends Callback {

        Boolean callback(IntByReference x1, IntByReference y1,
                         IntByReference x2, IntByReference y2,
                         IntByReference duration,
                         MaaTransparentArg handleArg);

    }

    interface TouchDown extends Callback {

        Boolean callback(IntByReference contact,
                         IntByReference x, IntByReference y,
                         IntByReference pressure,
                         MaaTransparentArg handleArg);

    }

    interface TouchMove extends Callback {

        Boolean callback(IntByReference contact,
                         IntByReference x, IntByReference y,
                         IntByReference pressure,
                         MaaTransparentArg handleArg);

    }

    interface TouchUp extends Callback {

        Boolean callback(IntByReference contact, MaaTransparentArg handleArg);

    }

    interface PressKey extends Callback {

        Boolean callback(IntByReference keycode, MaaTransparentArg handleArg);

    }

    interface InputText extends Callback {

        Boolean callback(MaaStringView text, MaaTransparentArg handleArg);

    }

}
