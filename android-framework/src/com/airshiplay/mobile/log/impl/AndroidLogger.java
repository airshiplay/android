package com.airshiplay.mobile.log.impl;

import java.util.IllegalFormatException;

import com.airshiplay.mobile.log.Logger;

import android.util.Log;

public class AndroidLogger implements Logger {
	protected String name;

	static final int TAG_MAX_LENGTH = 23;

	public AndroidLogger(String actualName) {
		this.name = actualName;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isTraceEnabled() {
		return Log.isLoggable(name, Log.VERBOSE);
	}

	@Override
	public void trace(String msg) {
		Log.v(name, msg);
	}

	@Override
	public void trace(String format, Object arg) {
		Log.v(name, format(format, arg));
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		Log.v(name, format(format, arg1, arg2));
	}

	@Override
	public void trace(String format, Object[] argArray) {
		Log.v(name, format(format, argArray));
	}

	@Override
	public void trace(String msg, Throwable t) {
		Log.v(name, msg, t);
	}

	@Override
	public boolean isDebugEnabled() {
		return Log.isLoggable(name, Log.DEBUG);
	}

	@Override
	public void debug(String msg) {
		Log.d(name, msg);
	}

	@Override
	public void debug(String format, Object arg) {
		Log.d(name, format(format, arg));
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		Log.d(name, format(format, arg1, arg2));
	}

	@Override
	public void debug(String format, Object[] argArray) {
		Log.d(name, format(format, argArray));
	}

	@Override
	public void debug(String msg, Throwable t) {
		Log.d(name, msg, t);
	}

	@Override
	public boolean isInfoEnabled() {
		return Log.isLoggable(name, Log.INFO);
	}

	@Override
	public void info(String msg) {
		Log.i(name, msg);
	}

	@Override
	public void info(String format, Object arg) {
		Log.i(name, format(format, arg));
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		Log.i(name, format(format, arg1, arg2));
	}

	@Override
	public void info(String format, Object[] argArray) {
		Log.i(name, format(format, argArray));
	}

	@Override
	public void info(String msg, Throwable t) {
		Log.i(name, msg, t);
	}

	@Override
	public boolean isWarnEnabled() {
		return Log.isLoggable(name, Log.WARN);
	}

	@Override
	public void warn(String msg) {
		Log.w(name, msg);
	}

	@Override
	public void warn(String format, Object arg) {
		Log.w(name, format(format, arg));
	}

	@Override
	public void warn(String format, Object[] argArray) {
		Log.w(name, format(format, argArray));
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		Log.w(name, format(format, arg1, arg2));
	}

	@Override
	public void warn(String msg, Throwable t) {
		Log.w(name, msg, t);
	}

	@Override
	public boolean isErrorEnabled() {
		return Log.isLoggable(name, Log.ERROR);
	}

	@Override
	public void error(String msg) {
		Log.e(name, msg);
	}

	@Override
	public void error(String format, Object arg) {
		Log.e(name, format(format, arg));
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		Log.e(name, format(format, arg1, arg2));
	}

	@Override
	public void error(String format, Object[] argArray) {
		Log.e(name, format(format, argArray));
	}

	@Override
	public void error(String msg, Throwable t) {
		Log.e(name, msg, t);
	}

	/**
	 * Returns a formatted string using the specified format string and
	 * arguments.
	 * 
	 * <p>
	 * The locale always used is the one returned by
	 * {@link java.util.Locale#getDefault() Locale.getDefault()}.
	 * 
	 * @param format
	 *            A <a href="../util/Formatter.html#syntax">format string</a>
	 * 
	 * @param args
	 *            Arguments referenced by the format specifiers in the format
	 *            string. If there are more arguments than format specifiers,
	 *            the extra arguments are ignored. The number of arguments is
	 *            variable and may be zero. The maximum number of arguments is
	 *            limited by the maximum dimension of a Java array as defined by
	 *            the <a href="http://java.sun.com/docs/books/vmspec/">Java
	 *            Virtual Machine Specification</a>. The behaviour on a
	 *            <tt>null</tt> argument depends on the <a
	 *            href="../util/Formatter.html#syntax">conversion</a>.
	 * 
	 * @throws IllegalFormatException
	 *             If a format string contains an illegal syntax, a format
	 *             specifier that is incompatible with the given arguments,
	 *             insufficient arguments given the format string, or other
	 *             illegal conditions. For specification of all possible
	 *             formatting errors, see the <a
	 *             href="../util/Formatter.html#detail">Details</a> section of
	 *             the formatter class specification.
	 * 
	 * @throws NullPointerException
	 *             If the <tt>format</tt> is <tt>null</tt>
	 * 
	 * @return A formatted string
	 * 
	 * @see java.util.Formatter
	 * @since 1.5
	 */
	private String format(String format, Object... objs) {
		return String.format(format, objs);
	}
}
