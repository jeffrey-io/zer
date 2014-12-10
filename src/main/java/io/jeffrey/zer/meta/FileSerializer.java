package io.jeffrey.zer.meta;

import java.io.File;

/**
 * Simple contract to lazily fix up uris
 *
 * @author jeffrey
 */
public interface FileSerializer {

    /**
     * convert the given URI into a normalized uri
     *
     * @param input
     *            the uri to make relative
     * @return the relative URI
     */
    public String normalilze(File input);
}
