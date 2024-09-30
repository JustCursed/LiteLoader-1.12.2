/*
 * This file is part of LiteLoader.
 * Copyright (C) 2012-16 Adam Mummery-Smith
 * All Rights Reserved.
 */
package com.mumfrey.liteloader.transformers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 * ClassWriter isolated from ASM so that it exists in the LaunchClassLoader
 *
 * @author Adam Mummery-Smith
 */
public class IsolatedClassWriter extends ClassWriter {
	public IsolatedClassWriter(int flags) {
		super(flags);
	}

	public IsolatedClassWriter(ClassReader classReader, int flags) {
		super(classReader, flags);
	}

	/**
	 * Returns the common super type of the two given types. The default
	 * implementation of this method <i>loads</i> the two given classes and uses
	 * the java.lang.Class methods to find the common super class. It can be
	 * overridden to compute this common super type in other ways, in particular
	 * without actually loading any class, or to take into account the class
	 * that is currently being generated by this ClassWriter, which can of
	 * course not be loaded since it is under construction.
	 *
	 * @param type1 the internal name of a class.
	 * @param type2 the internal name of another class.
	 * @return the internal name of the common super class of the two given
	 * classes.
	 */
	@Override
	protected String getCommonSuperClass(final String type1, final String type2) {
		Class<?> c, d;
		ClassLoader classLoader = getClass().getClassLoader();
		try {
			c = Class.forName(type1.replace('/', '.'), false, classLoader);
			d = Class.forName(type2.replace('/', '.'), false, classLoader);
		} catch (Exception e) {
			throw new RuntimeException(e.toString(), e);
		}

		if (c.isAssignableFrom(d)) {
			return type1;
		}
		if (d.isAssignableFrom(c)) {
			return type2;
		}
		if (c.isInterface() || d.isInterface()) {
			return "java/lang/Object";
		}
		do {
			c = c.getSuperclass();
		}
		while (!c.isAssignableFrom(d));

		return c.getName().replace('.', '/');
	}
}
