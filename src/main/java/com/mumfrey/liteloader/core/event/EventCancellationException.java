/*
 * This file is part of LiteLoader.
 * Copyright (C) 2012-16 Adam Mummery-Smith
 * All Rights Reserved.
 */
package com.mumfrey.liteloader.core.event;

public class EventCancellationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EventCancellationException() {
	}

	public EventCancellationException(String message) {
		super(message);
	}

	public EventCancellationException(Throwable cause) {
		super(cause);
	}

	public EventCancellationException(String message, Throwable cause) {
		super(message, cause);
	}
}
