package io.jeffrey.zer.meta;

import java.io.File;

/**
 * Simple contract to lazily fix up uris
 *
 * @author jeffrey
 */
public interface DocumentFileSystem {

    /**
     * Convert the nice uri into a file on the disk
     *
     * @param path
     *            the serialized and nice uri
     * @return a File object
     */
    public File find(String path);

    /**
     * convert the given URI into a normalized uri
     *
     * @param input
     *            the uri to make relative
     * @return the relative URI
     */
    public String normalize(File input);
}
