package com.arishiplay.mobile.android.framework.bean;

/**
 * @author lig
 * @version 1.0
 * @since 1.0 2013-2-6
 */
public class ApkBean {
	public static final int DOWNLOAD = 1;
	public static final int INSTALLED = 2;
	private String file = null;
	private String path = null;
	private String pkgName = null;
	private int state = 1;
	private String versionCode = null;
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPkgName() {
		return pkgName;
	}
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	
	
}
