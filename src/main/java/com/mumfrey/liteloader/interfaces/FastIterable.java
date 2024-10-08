/*
 * This file is part of LiteLoader.
 * Copyright (C) 2012-16 Adam Mummery-Smith
 * All Rights Reserved.
 */
package com.mumfrey.liteloader.interfaces;

/**
 * Interface for objects which can return a baked list view of their list
 * contents.
 *
 * @param <T>
 * @author Adam Mummery-Smith
 */
public interface FastIterable<T> extends Iterable<T> {
	/**
	 * Add an entry to the iterable
	 *
	 * @param entry
	 */
	public boolean add(T entry);

	/**
	 * Return the baked view of all entries
	 */
	public T all();

	/**
	 * Invalidate (force rebake of) the baked entry list
	 */
	public void invalidate();
}
