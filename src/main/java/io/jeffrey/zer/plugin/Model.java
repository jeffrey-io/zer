package io.jeffrey.zer.plugin;

/**
 * This defines the underlying bridge to query the various document with minimal functions
 *
 * @author jeffrey
 *
 */
public interface Model {

    /**
     * denote that we are about to manipulate data (to preserve history and ensure undo/redo work)
     */
    public void begin();

    /**
     * indicate that we are no longer doing anything
     */
    public void end();

    /**
     * Execute a query against the model; the query language is up to the underlying tool
     *
     * @param query
     *            a query in the underlying tool's language
     * @return a string representation of the result
     */
    public String getJson(String query);

    /**
     * invoke the method and return json
     *
     * @param query
     *            what to operate on
     * @param method
     *            the method to invoke
     */
    public String invokeAndReturnJson(String query, String method);

    /**
     * write a value to the given query
     *
     * @param query
     *            the query (in the underlying tool's language)
     * @param value
     *            the value to write
     */
    public void put(String query, String value);
}
