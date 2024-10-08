/*
 * This file is part of LiteLoader.
 * Copyright (C) 2012-16 Adam Mummery-Smith
 * All Rights Reserved.
 */
package com.mumfrey.liteloader.core;

import com.mumfrey.liteloader.api.Listener;

import java.util.List;

/**
 * Common interface for the client/server plugin channel listeners. <b>Do not
 * implement this interface directly</b>, nothing will happen!
 *
 * @author Adam Mummery-Smith
 */
public interface CommonPluginChannelListener extends Listener {
	/**
	 * Return a list of the plugin channels the mod wants to register.
	 *
	 * @return plugin channel names as a list, it is recommended to use
	 * {@link com.google.common.collect.ImmutableList#of} for this purpose
	 */
	public abstract List<String> getChannels();
}
