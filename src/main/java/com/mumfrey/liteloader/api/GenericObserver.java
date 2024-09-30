/*
 * This file is part of LiteLoader.
 * Copyright (C) 2012-16 Adam Mummery-Smith
 * All Rights Reserved.
 */
package com.mumfrey.liteloader.api;

/**
 * Generic Observer class, for Intra-API Observer inking
 *
 * @param <T> Argument type for observable events
 * @author Adam Mummery-Smith
 */
public interface GenericObserver<T> extends Observer {
	public abstract void onObservableEvent(String eventName, T... eventArgs) throws ClassCastException;
}
