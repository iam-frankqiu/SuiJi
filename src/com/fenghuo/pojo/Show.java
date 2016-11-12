package com.fenghuo.pojo;

public class Show {

	private int uid;
	private String name;
	private int theme;
	
	public int getTheme() {
		return theme;
	}

	public void setTheme(int theme) {
		this.theme = theme;
	}

	public Show(int uid, String name, int theme, String headicon, String time,
			String position, String image) {
		super();
		this.uid = uid;
		this.name = name;
		this.theme = theme;
		this.headicon = headicon;
		this.time = time;
		this.position = position;
		this.image = image;
	}

	public Show(int uid, String name, String headicon, String time,
			String position, String image) {
		super();
		this.uid = uid;
		this.name = name;
		this.headicon = headicon;
		this.time = time;
		this.position = position;
		this.image = image;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private String headicon;
	private String time;
	private String position;
	private String image;
	
	public Show() {
		super();
	}
	public Show(int uid, String headicon, String time, String position,
			String image) {
		super();
		this.uid = uid;
		this.headicon = headicon;
		this.time = time;
		this.position = position;
		this.image = image;
	}
	@Override
	public String toString() {
		return "Show [uid=" + uid + ", name=" + name + ", theme=" + theme
				+ ", headicon=" + headicon + ", time=" + time + ", position="
				+ position + ", image=" + image + "]";
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getHeadicon() {
		return headicon;
	}
	public void setHeadicon(String headicon) {
		this.headicon = headicon;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
}
