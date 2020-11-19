package myns.histbatch.watcher;

/**
 * Interface for grouping item types.
 * 
 * This can not be a marker interface, because some processors
 * need something like the <code>shortName</code> to do post-actions.
 */
public interface IncomingItemType {

    /**
     * Provides a short name representing this type (e. g. 'text')
     * 
     * @return The short name
     */
    public String shortName();
    
}
