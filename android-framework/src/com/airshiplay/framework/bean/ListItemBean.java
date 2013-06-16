package com.airshiplay.framework.bean;

public class ListItemBean {
	public String title;
	public int id;

	public ListItemBean(int id, String title) {
		this.title = title;
		this.id = id;
	}

	@Override
	public String toString() {
		return title;
	}
}
