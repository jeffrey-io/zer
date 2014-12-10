package io.jeffrey.zer;

import java.util.HashSet;

/**
 * A very naive implementation of Syncable that treats a set of Syncable as a Syncable
 *
 * @author jeffrey
 */
public class SyncableSet implements Syncable {

    private final HashSet<Syncable> all;

    /**
     * create an empty set
     */
    public SyncableSet() {
        all = new HashSet<>();
    }

    /**
     * Link the current syncable to this
     *
     * @param s
     *            the syncable to add
     */
    public void add(final Syncable s) {
        all.add(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sync() {
        for (final Syncable s : all) {
            s.sync();
        }
    }
}
