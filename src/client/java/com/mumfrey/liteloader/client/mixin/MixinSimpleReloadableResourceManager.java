/*
 * This file is part of LiteLoader.
 * Copyright (C) 2012-16 Adam Mummery-Smith
 * All Rights Reserved.
 */
package com.mumfrey.liteloader.client.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.mumfrey.liteloader.client.ducks.IReloadable;

import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;

@Mixin(SimpleReloadableResourceManager.class)
public abstract class MixinSimpleReloadableResourceManager implements IReloadable {
	@Shadow
	@Final
	private List<IResourceManagerReloadListener> reloadListeners;

	@Override
	public List<IResourceManagerReloadListener> getReloadListeners() {
		return this.reloadListeners;
	}
}
