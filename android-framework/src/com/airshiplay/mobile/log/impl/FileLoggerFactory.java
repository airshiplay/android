/*
 * Created 21.10.2009
 *
 * Copyright (c) 2009 SLF4J.ORG
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.airshiplay.mobile.log.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.airshiplay.mobile.log.ILoggerFactory;
import com.airshiplay.mobile.util.SystemUtil;

public class FileLoggerFactory implements ILoggerFactory {
	private final Map<String, FileLogger> loggerMap;
	protected String fileName = null;
	protected QuietWriter qw;
	private List<String> buffer;
	private Context mContext;

	public FileLoggerFactory(Context context) throws FileNotFoundException {
		mContext = context;
		loggerMap = new HashMap<String, FileLogger>();

		setFile(SystemUtil.getExternalBaseStorePath().getAbsolutePath() + "/log.log", true, true, 5000);
		buffer = new ArrayList<String>();
		new Thread(new Dispatcher()).start();
	}

	public FileLogger getLogger(final String name) {
		final String actualName = (name);
		FileLogger slogger = null;
		synchronized (this) {
			slogger = loggerMap.get(actualName);
			if (slogger == null) {
				slogger = new FileLogger(actualName, buffer);
				loggerMap.put(actualName, slogger);
			}
		}
		return slogger;
	}

	public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize) throws FileNotFoundException {
		reset();
		FileOutputStream ostream = null;
		try {
			ostream = new FileOutputStream(fileName, append);
		} catch (FileNotFoundException ex) {
			String parentName = new File(fileName).getParent();
			if (parentName != null) {
				File parentDir = new File(parentName);
				if (!parentDir.exists() && parentDir.mkdirs()) {
					ostream = new FileOutputStream(fileName, append);
				} else {
					throw ex;
				}
			} else {
				throw ex;
			}
		}
		Writer fw = createWriter(ostream);
		if (bufferedIO) {
			fw = new BufferedWriter(fw, bufferSize);
		}
		this.qw = new QuietWriter(fw);
		writeHeader();
	}

	protected void writeHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		try {
			PackageManager pm = mContext.getPackageManager();
			PackageInfo info = pm.getPackageInfo(mContext.getPackageName(), 0);
			sb.append("\n应用程序名称:" + pm.getApplicationLabel(mContext.getApplicationInfo()));
			sb.append("\n应用程序版本名称:" + info.versionName);
			sb.append("\n应用程序版本号:" + info.versionCode);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		sb.append("\n");
		if (this.qw != null)
			this.qw.write(sb.toString());
	}

	protected void reset() {
		closeFile();
		this.fileName = null;
		closeWriter();
	}

	protected void closeFile() {
		if (this.qw != null) {
			try {
				this.qw.close();
			} catch (java.io.IOException e) {
				if (e instanceof InterruptedIOException) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	protected void closeWriter() {
		if (qw != null) {
			try {
				qw.close();
			} catch (IOException e) {
				if (e instanceof InterruptedIOException) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	protected OutputStreamWriter createWriter(OutputStream os) {
		OutputStreamWriter retval = null;
		retval = new OutputStreamWriter(os);
		return retval;
	}

	private class Dispatcher implements Runnable {

		@Override
		public void run() {
			boolean isActive = true;
			try {
				while (isActive) {
					String[] tmp;
					synchronized (buffer) {
						while ((buffer.size() == 0) && isActive) {
							buffer.wait();
						}
						tmp = buffer.toArray(new String[0]);
						buffer.clear();
						buffer.notifyAll();
					}
					if (tmp != null) {
						for (int i = 0; i < tmp.length; i++) {
							qw.write(tmp[i]);
						}
						qw.flush();
					}
				}
			} catch (Exception ex) {
				Thread.currentThread().interrupt();
			}

		}

	}
}
