package com.airshiplay.mobile.log.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IllegalFormatException;
import java.util.List;

import android.util.Log;

import com.airshiplay.mobile.log.Logger;

public class FileLogger implements Logger {
	protected String name;
	private List<String> buffer;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public FileLogger(String actualName, List<String> bff) {
		this.name = actualName;
		this.buffer = bff;
	}

	private String getTag() {
		try {
			StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
			StackTraceElement stack = stacks[5];
			StringBuffer buffer = new StringBuffer();
			buffer.append("[" + stack.getClassName());
			buffer.append(".");
			buffer.append(stack.getMethodName());
			buffer.append(":" + stack.getLineNumber() + "]");
			return buffer.toString();
		} catch (Exception e) {
			return "[?:?]";
		}
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
		outputlog(Log.VERBOSE, msg, null);
	}

	@Override
	public void trace(String format, Object arg) {
		outputlog(Log.VERBOSE, format(format, arg), null);
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		outputlog(Log.VERBOSE, format(format, arg1, arg2), null);
	}

	@Override
	public void trace(String format, Object[] argArray) {
		outputlog(Log.VERBOSE, format(format, argArray), null);
	}

	@Override
	public void trace(String msg, Throwable t) {
		outputlog(Log.VERBOSE, msg, t);
	}

	@Override
	public boolean isDebugEnabled() {
		return Log.isLoggable(name, Log.DEBUG);
	}

	@Override
	public void debug(String msg) {
		outputlog(Log.DEBUG, msg, null);
	}

	@Override
	public void debug(String format, Object arg) {
		outputlog(Log.DEBUG, format(format, arg), null);
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		outputlog(Log.DEBUG, format(format, arg1, arg2), null);
	}

	@Override
	public void debug(String format, Object[] argArray) {
		outputlog(Log.DEBUG, format(format, argArray), null);
	}

	@Override
	public void debug(String msg, Throwable t) {
		outputlog(Log.DEBUG, msg, t);
	}

	@Override
	public boolean isInfoEnabled() {
		return Log.isLoggable(name, Log.INFO);
	}

	@Override
	public void info(String msg) {
		outputlog(Log.INFO, msg, null);
	}

	@Override
	public void info(String format, Object arg) {
		outputlog(Log.INFO, format(format, arg), null);
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		outputlog(Log.INFO, format(format, arg1, arg2), null);
	}

	@Override
	public void info(String format, Object[] argArray) {
		outputlog(Log.INFO, format(format, argArray), null);
	}

	@Override
	public void info(String msg, Throwable t) {
		outputlog(Log.INFO, msg, t);
	}

	@Override
	public boolean isWarnEnabled() {
		return Log.isLoggable(name, Log.WARN);
	}

	@Override
	public void warn(String msg) {
		outputlog(Log.WARN, msg, null);
	}

	@Override
	public void warn(String format, Object arg) {
		outputlog(Log.WARN, format(format, arg), null);
	}

	@Override
	public void warn(String format, Object[] argArray) {
		outputlog(Log.WARN, format(format, argArray), null);
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		outputlog(Log.WARN, format(format, arg1, arg2), null);
	}

	@Override
	public void warn(String msg, Throwable t) {
		outputlog(Log.WARN, msg, t);
	}

	@Override
	public boolean isErrorEnabled() {
		return Log.isLoggable(name, Log.ERROR);
	}

	@Override
	public void error(String msg) {
		outputlog(Log.ERROR, msg, null);
	}

	@Override
	public void error(String format, Object arg) {
		outputlog(Log.ERROR, format(format, arg), null);
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		outputlog(Log.ERROR, format(format, arg1, arg2), null);
	}

	@Override
	public void error(String format, Object[] argArray) {
		outputlog(Log.ERROR, format(format, argArray), null);
	}

	@Override
	public void error(String msg, Throwable t) {
		outputlog(Log.ERROR, msg, t);
	}

	private void outputlog(int logLevel, String msg, Throwable t) {
		StringWriter sw = new StringWriter();

		sw.write(sdf.format(new Date()));
		switch (logLevel) {
		case Log.ASSERT:
			sw.write(" ASSERT ");
			break;
		case Log.DEBUG:
			sw.write(" DEBUG ");
			break;
		case Log.ERROR:
			sw.write(" ERROR ");
			break;
		case Log.INFO:
			sw.write(" INFO ");
			break;
		case Log.VERBOSE:
			sw.write(" VERBOSE ");
			break;
		case Log.WARN:
			sw.write(" WARN ");
			break;
		default:
			break;
		}
		sw.write(getTag() + " ");
		if (msg != null)
			sw.write(msg);
		if (t != null)
			t.printStackTrace(new PrintWriter(sw));
		sw.append('\n');
		synchronized (buffer) {
			buffer.add(sw.toString());
			buffer.notifyAll();
		}
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
