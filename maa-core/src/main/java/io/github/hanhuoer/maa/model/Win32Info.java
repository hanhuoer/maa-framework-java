package io.github.hanhuoer.maa.model;

import io.github.hanhuoer.maa.ptr.MaaWin32Hwnd;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author H
 */
@Data
@Accessors(chain = true)
public class Win32Info {

    private String className;
    private String windowName;
    private MaaWin32Hwnd hwnd;

}
