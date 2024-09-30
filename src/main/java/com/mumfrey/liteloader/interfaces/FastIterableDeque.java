/*
 * This file is part of LiteLoader.
 * Copyright (C) 2012-16 Adam Mummery-Smith
 * All Rights Reserved.
 */
package com.mumfrey.liteloader.interfaces;

import java.util.Deque;

/**
 * Deque interface which is FastIterable
 *
 * @param <T>
 * @author Adam Mummery-Smith
 */
public interface FastIterableDeque<T> extends FastIterable<T>, Deque<T> {
}
