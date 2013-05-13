package com.airshiplay.framework.bean;

public interface BaseBean {

	/**
	 * All Download Resources(Application,Music,Recommended Application,Game
	 * Application etc...) Uniquely identifies
	 * 
	 * @return
	 */
	public String getResId();

	public String getIdentifier();

	/**
	 * Resource Name
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Resource Download Url
	 * 
	 * @return
	 */
	public String getDownloadUrl();

	public int getAct();

	public String getCateName();

	/**
	 * Resource Size : 10KB or 10B or 10MB etc...
	 * 
	 * @return
	 */
	public String getSize();

	public String getVersionCode();

	public String getVersionName();

	public long getSizeValue();

}
