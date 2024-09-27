package io.github.hanhuoer.maa.model;

import io.github.hanhuoer.maa.define.MaaToolkitDesktopWindowHandle;
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
    private MaaToolkitDesktopWindowHandle handle;

}
