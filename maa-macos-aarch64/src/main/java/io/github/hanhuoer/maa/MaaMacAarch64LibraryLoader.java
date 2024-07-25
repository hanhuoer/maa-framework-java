package io.github.hanhuoer.maa;

import io.github.hanhuoer.maa.loader.MaaLibraryLoader;
import io.github.hanhuoer.maa.util.JarFileUtil;

import java.io.IOException;

/**
 * @author H
 */
public class MaaMacAarch64LibraryLoader implements MaaLibraryLoader {

    @Override
    public void loadLibrary() throws IOException {
        JarFileUtil.copyDirectory("mac_aarch64", "/lib");
    }

    @Override
    public String getMaaFrameworkName() {
        return "libMaaFramework.dylib";
    }

    @Override
    public String getMaaToolkitName() {
        return "libMaaToolkit.dylib";
    }
}
