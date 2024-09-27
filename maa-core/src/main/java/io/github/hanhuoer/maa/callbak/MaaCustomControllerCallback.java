package io.github.hanhuoer.maa.callbak;

import com.sun.jna.Callback;
import io.github.hanhuoer.maa.define.MaaImageBufferHandle;
import io.github.hanhuoer.maa.define.MaaStringBufferHandle;
import io.github.hanhuoer.maa.define.MaaStringView;
import io.github.hanhuoer.maa.define.MaaTransparentArg;
import io.github.hanhuoer.maa.define.base.MaaInteger;

/**
 * MaaCustomController.h MaaCustomControllerCallbacks
 *
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
                         MaaInteger width, MaaInteger height);

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

        Boolean callback(MaaInteger x, MaaInteger y, MaaTransparentArg handleArg);

    }

    interface Swipe extends Callback {

        Boolean callback(MaaInteger x1, MaaInteger y1,
                         MaaInteger x2, MaaInteger y2,
                         MaaInteger duration,
                         MaaTransparentArg handleArg);

    }

    interface TouchDown extends Callback {

        Boolean callback(MaaInteger contact,
                         MaaInteger x, MaaInteger y,
                         MaaInteger pressure,
                         MaaTransparentArg handleArg);

    }

    interface TouchMove extends Callback {

        Boolean callback(MaaInteger contact,
                         MaaInteger x, MaaInteger y,
                         MaaInteger pressure,
                         MaaTransparentArg handleArg);

    }

    interface TouchUp extends Callback {

        Boolean callback(MaaInteger contact, MaaTransparentArg handleArg);

    }

    interface PressKey extends Callback {

        Boolean callback(MaaInteger keycode, MaaTransparentArg handleArg);

    }

    interface InputText extends Callback {

        Boolean callback(MaaStringView text, MaaTransparentArg handleArg);

    }

}
