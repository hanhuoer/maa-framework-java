package io.github.hanhuoer.maa.model;

import io.github.hanhuoer.maa.define.MaaAdbInputMethod;
import io.github.hanhuoer.maa.define.MaaAdbScreencapMethod;
import io.github.hanhuoer.maa.define.MaaToolkitAdbDeviceHandle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author H
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AdbInfo {

    private MaaToolkitAdbDeviceHandle device;

    private String name;
    private String path;
    private String config;
    private String address;
    private MaaAdbInputMethod inputMethod;
    private MaaAdbScreencapMethod screencapMethod;

}
