/*
 * This file is part of LiteLoader.
 * Copyright (C) 2012-16 Adam Mummery-Smith
 * All Rights Reserved.
 */
package com.mumfrey.liteloader;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.server.SPacketJoinGame;


/**
 * Interface for mods which wish to be notified when the player connects to a
 * server (or local game).
 *
 * @author Adam Mummery-Smith
 */
public interface PreJoinGameListener extends LiteMod {
	/**
	 * Called before login. NOTICE: as of 1.8 the return value of this method
	 * has a different meaning!
	 *
	 * @param netHandler     Net handler
	 * @param joinGamePacket Join game packet
	 * @return true to allow login to continue, false to cancel login
	 */
	public abstract boolean onPreJoinGame(INetHandler netHandler, SPacketJoinGame joinGamePacket);
}
