package com.airshiplay.framework.http;

import java.io.IOException;

public interface RequestListener<T> {
	public abstract void onComplete(T t);
	public abstract void onIOException(IOException paramIOException);
}
