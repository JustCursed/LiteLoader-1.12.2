/*
 * This file is part of LiteLoader.
 * Copyright (C) 2012-16 Adam Mummery-Smith
 * All Rights Reserved.
 */
package com.mumfrey.liteloader.api;

/**
 * Observer for interface binding events
 *
 * @author Adam Mummery-Smith
 */
public interface InterfaceObserver extends Observer {
	public void onRegisterListener(InterfaceProvider provider, Class<? extends Listener> interfaceType, Listener listener);
}
