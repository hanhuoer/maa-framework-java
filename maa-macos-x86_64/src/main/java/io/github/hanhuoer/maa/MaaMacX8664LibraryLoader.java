package io.github.hanhuoer.maa;

import io.github.hanhuoer.maa.loader.MaaLibraryLoader;
import io.github.hanhuoer.maa.util.JarFileUtil;

import java.io.IOException;

/**
 * @author H
 */
public class MaaMacX8664LibraryLoader implements MaaLibraryLoader {

    @Override
    public void loadLibrary() throws IOException {
        JarFileUtil.copyDirectory("mac_x86_64", "/lib");
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
