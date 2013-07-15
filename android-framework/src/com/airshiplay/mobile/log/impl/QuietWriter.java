package com.airshiplay.mobile.log.impl;

import java.io.FilterWriter;
import java.io.Writer;

public class QuietWriter extends FilterWriter {

	public QuietWriter(Writer writer) {
		super(writer);
	}
	public void write(String string) {
		if (string != null) {
			try {
				out.write(string);
			} catch (Exception e) {
			}
		}
	}
	public void flush() {
		try {
			out.flush();
		} catch (Exception e) {
		}
	}
}
