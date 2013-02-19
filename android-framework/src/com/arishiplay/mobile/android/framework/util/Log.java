package com.arishiplay.mobile.android.framework.util;

import java.io.PrintWriter;

import com.arishiplay.mobile.android.framework.reflect.Method;

/**
 * @author lig
 * @version 1.0
 * @since 1.0 2013-2-6
 */
public class Log {
	private String tag;

	public Log(String tag) {
		this.tag = tag;
	}

	public void debug(String msg) {
		android.util.Log.d(tag, msg);
	}

	public void debug(String msg, Throwable tr) {
		android.util.Log.d(tag, msg, tr);
		printStackTrace(new StringBuffer(), "", tr, null);
	}

	/**
	 * java 版本
	 * 
	 * @param s
	 * @param tr
	 */
	public void printStackTrace(PrintWriter s, Throwable tr) {
		synchronized (s) {
			s.println(this);
			StackTraceElement[] trace = tr.getStackTrace();
			for (int i = 0; i < trace.length; i++)
				s.println("\tat " + trace[i]);

			Throwable ourCause = tr.getCause();
			if (ourCause != null)
				printStackTraceAsCause(s, ourCause, trace);
		}
	}

	/**
	 * Print our stack trace as a cause for the specified stack trace.
	 */
	private void printStackTraceAsCause(PrintWriter s, Throwable tr,
			StackTraceElement[] causedTrace) {
		// assert Thread.holdsLock(s);

		// Compute number of frames in common between this and caused
		StackTraceElement[] trace = tr.getStackTrace();
		int m = trace.length - 1, n = causedTrace.length - 1;
		while (m >= 0 && n >= 0 && trace[m].equals(causedTrace[n])) {
			m--;
			n--;
		}
		int framesInCommon = trace.length - 1 - m;

		s.println("Caused by: " + this);
		for (int i = 0; i <= m; i++)
			s.println("\tat " + trace[i]);
		if (framesInCommon != 0)
			s.println("\t... " + framesInCommon + " more");

		// Recurse if we have a cause
		Throwable ourCause = tr.getCause();
		if (ourCause != null)
			printStackTraceAsCause(s, ourCause, trace);
	}

	/**
	 * android 版本
	 * 
	 * @param err
	 * @param indent
	 *            ""
	 * @param tr
	 * @param parentStack
	 *            null
	 */
	public void printStackTrace(StringBuffer err, String indent, Throwable tr,
			StackTraceElement[] parentStack) {
		String msg = tr.getLocalizedMessage();
		String name = getClass().getName();
		err.append(name);
		if (msg != null)
			err.append(":" + msg);
		err.append("\n");

		StackTraceElement[] stack = tr.getStackTrace();
		if (stack != null) {
			int duplicates = parentStack != null ? countDuplicates(stack,
					parentStack) : 0;
			for (int i = 0; i < stack.length - duplicates; i++) {
				err.append(indent);
				err.append("\tat ");
				err.append(stack[i].toString());
				err.append("\n");
			}

			if (duplicates > 0) {
				err.append(indent);
				err.append("\t... ");
				err.append(Integer.toString(duplicates));
				err.append(" more\n");
			}
		}

		// Print suppressed exceptions indented one level deeper.
		Object o = Method.invoke(tr, "getSuppressed");
		if (o != null) {
			Throwable[] suppressedExceptions = (Throwable[]) o;
			for (Throwable throwable : suppressedExceptions) {
				err.append(indent);
				err.append("\tSuppressed: ");
				printStackTrace(err, indent + "\t", throwable, stack);
			}
		}

		Throwable cause = tr.getCause();
		if (cause != null) {
			err.append(indent);
			err.append("Caused by: ");
			printStackTrace(err, indent, cause, stack);
		}
	}

	/**
	 * Counts the number of duplicate stack frames, starting from the end of the
	 * stack.
	 * 
	 * @param currentStack
	 *            a stack to compare
	 * @param parentStack
	 *            a stack to compare
	 * 
	 * @return the number of duplicate stack frames.
	 */
	private static int countDuplicates(StackTraceElement[] currentStack,
			StackTraceElement[] parentStack) {
		int duplicates = 0;
		int parentIndex = parentStack.length;
		for (int i = currentStack.length; --i >= 0 && --parentIndex >= 0;) {
			StackTraceElement parentFrame = parentStack[parentIndex];
			if (parentFrame.equals(currentStack[i])) {
				duplicates++;
			} else {
				break;
			}
		}
		return duplicates;
	}
}
