package net.anjeg.utils.reflection;

public interface Visitor<T> {

	// ////////////////////////////////////////////////////////////////////////
	// Methods
	// ////////////////////////////////////////////////////////////////////////
	
	/**
     * @return {@code true} if the algorithm should visit more results,
     * {@code false} if it should terminate now.
     */
    public boolean visit(T t);
    
}
