/*
 * This file is part of LiteLoader.
 * Copyright (C) 2012-16 Adam Mummery-Smith
 * All Rights Reserved.
 */
package com.mumfrey.liteloader.core.api;

import com.mumfrey.liteloader.api.ModClassValidator;

import java.util.List;

public class DefaultClassValidator<T> implements ModClassValidator {
	private final Class<T> superClass;

	private final List<String> supportedPrefixes;

	public DefaultClassValidator(Class<T> superClass, List<String> supportedPrefixes) {
		this.supportedPrefixes = supportedPrefixes;
		this.superClass = superClass;
	}

	@Override
	public boolean validateName(String className) {
		return this.supportedPrefixes == null
			|| this.supportedPrefixes.size() == 0
			|| DefaultClassValidator.startsWithAny(className, this.supportedPrefixes);
	}

	@Override
	public boolean validateClass(ClassLoader classLoader, Class<?> candidateClass) {
		return (candidateClass != null
			&& !this.superClass.equals(candidateClass)
			&& this.superClass.isAssignableFrom(candidateClass)
			&& !candidateClass.isInterface());
	}

	private static boolean startsWithAny(String string, List<String> candidates) {
		for (String candidate : candidates) {
			if (string.startsWith(candidate)) {
				return true;
			}
		}

		return false;
	}
}
