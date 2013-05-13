package com.airshiplay.framework.bean;

/**
 * @author lig
 * @version 1.0
 * @since 1.0 2013-2-6
 */
public class AbstractBaseBean implements BaseBean {
	public int act;
	public String cateName;
	public String downloadUrl;
	public String identifier;
	public String name;
	public String price;
	public String resId;
	public String size;
	public String versionCode;
	public String versionName;

	public long getSizeValue() {
		return 0;
	}

	public int getAct() {
		return act;
	}

	public void setAct(int act) {
		this.act = act;
	}

	public String getCateName() {
		return cateName;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

}
