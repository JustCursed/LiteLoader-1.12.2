/*
 * This file is part of LiteLoader.
 * Copyright (C) 2012-16 Adam Mummery-Smith
 * All Rights Reserved.
 */
package com.mumfrey.liteloader.api;

/**
 * LiteLoader Extensible API - ShutDownObserver
 * <p>
 * ShutDownObservers receive an event when the game is shutting down due to a
 * user request. They do NOT receive the callback when the VM is terminating,
 * use a regular VM shutdownhook for that.
 *
 * @author Adam Mummery-Smith
 */
public interface ShutdownObserver extends Observer {
	public abstract void onShutDown();
}
