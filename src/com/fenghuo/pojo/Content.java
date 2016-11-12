package com.fenghuo.pojo;



public class Content {
private int id;
private int uid;
private String image;
private String msg;
private String position;
private String time;
private String video;
private int theme;
private String title;
public Content() {
	super();
}
public Content(int id, int uid, String image, String msg, String position,
		String time, String video, int theme, String title) {
	super();
	this.id = id;
	this.uid = uid;
	this.image = image;
	this.msg = msg;
	this.position = position;
	this.time = time;
	this.video = video;
	this.theme = theme;
	this.title = title;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getUid() {
	return uid;
}
public void setUid(int uid) {
	this.uid = uid;
}
public String getImage() {
	return image;
}
public void setImage(String image) {
	this.image = image;
}
public String getMsg() {
	return msg;
}
public void setMsg(String msg) {
	this.msg = msg;
}
public String getPosition() {
	return position;
}
public void setPosition(String position) {
	this.position = position;
}
public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}
public String getVideo() {
	return video;
}
public void setVideo(String video) {
	this.video = video;
}
public int getTheme() {
	return theme;
}
public void setTheme(int theme) {
	this.theme = theme;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
@Override
public String toString() {
	return "Content [id=" + id + ", uid=" + uid + ", image=" + image + ", msg="
			+ msg + ", position=" + position + ", time=" + time + ", video="
			+ video + ", theme=" + theme + ", title=" + title + "]";
}

}
