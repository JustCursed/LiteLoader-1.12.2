/*
 * This file is part of LiteLoader.
 * Copyright (C) 2012-16 Adam Mummery-Smith
 * All Rights Reserved.
 */
package com.mumfrey.liteloader.util;

import com.mumfrey.liteloader.core.runtime.Obf;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import top.outlands.foundation.TransformerDelegate;

public class ObfuscationUtilities {
	/**
	 * True if FML is being used, in which case we use searge names instead of
	 * raw field/method names.
	 */
	private static boolean fmlDetected = false;

	private static boolean checkedObfEnv = false;
	private static boolean seargeNames = false;

	static {
		// Check for FML
		ObfuscationUtilities.fmlDetected = ObfuscationUtilities.fmlIsPresent();
	}

	public static boolean fmlIsPresent() {
		for (IClassTransformer transformer : TransformerDelegate.getTransformers()) {
			if (transformer.getClass().getName().contains("fml")) {
				return true;
			}
		}

		return false;
	}

	public static boolean useSeargeNames() {
		if (!ObfuscationUtilities.checkedObfEnv) {
			ObfuscationUtilities.checkedObfEnv = true;

			try {
				MinecraftServer.class.getDeclaredField("serverRunning");
			} catch (SecurityException ex) {
			} catch (NoSuchFieldException ex) {
				ObfuscationUtilities.seargeNames = true;
			}
		}

		return ObfuscationUtilities.seargeNames;
	}

	/**
	 * Abstraction helper function
	 *
	 * @param fieldName Name of field to get, returned unmodified if in debug
	 *                  mode
	 * @return Obfuscated field name if present
	 */
	public static String getObfuscatedFieldName(String fieldName, String obfuscatedFieldName, String seargeFieldName) {
		boolean deobfuscated = BlockPos.class.getSimpleName().equals("BlockPos");
		return deobfuscated ? (ObfuscationUtilities.useSeargeNames() ? seargeFieldName : fieldName)
			: (ObfuscationUtilities.fmlDetected ? seargeFieldName : obfuscatedFieldName);
	}

	/**
	 * Abstraction helper function
	 *
	 * @param obf Field to get, returned unmodified if in debug mode
	 * @return Obfuscated field name if present
	 */
	public static String getObfuscatedFieldName(Obf obf) {
		boolean deobfuscated = BlockPos.class.getSimpleName().equals("BlockPos");
		return deobfuscated ? (ObfuscationUtilities.useSeargeNames() ? obf.srg : obf.name) : (ObfuscationUtilities.fmlDetected ? obf.srg : obf.obf);
	}
}
