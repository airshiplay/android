package com.airshiplay.framework.bean;

import java.io.Serializable;

/**
 * @author lig
 * @version 1.0
 * @since 1.0 2013-2-6
 */
public class DownLoadBean implements Serializable {
	private static final long serialVersionUID = 517044765088563807L;
	public String downloadUrl;
	public String fileUrl;
	public String identifier;
	public String price;
	public String resId;
	public long size;
	public boolean trustCdn;
	public String versionCode;
	public String versionName;
}
