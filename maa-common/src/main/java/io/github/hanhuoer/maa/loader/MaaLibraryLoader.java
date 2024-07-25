package io.github.hanhuoer.maa.loader;

/**
 * @author H
 */
public interface MaaLibraryLoader extends LibraryLoader {

    /**
     * get the file name of the native lib.
     */
    String getMaaFrameworkName();

    /**
     * get the file name of the native lib.
     */
    String getMaaToolkitName();

}
