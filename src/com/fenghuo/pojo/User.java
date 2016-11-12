package com.fenghuo.pojo;

public class User {
private int id;
private String name;
private String headicon;
private String pass;
private int pnumber;
public User(int id, String name, String headicon, String pass, int pnumber) {
	super();
	this.id = id;
	this.name = name;
	this.headicon = headicon;
	this.pass = pass;
	this.pnumber = pnumber;
}
public User() {
	super();
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getHeadicon() {
	return headicon;
}
public void setHeadicon(String headicon) {
	this.headicon = headicon;
}
public String getPass() {
	return pass;
}
public void setPass(String pass) {
	this.pass = pass;
}
public int getPnumber() {
	return pnumber;
}
public void setPnumber(int pnumber) {
	this.pnumber = pnumber;
}
@Override
public String toString() {
	return "User [id=" + id + ", name=" + name + ", headicon=" + headicon
			+ ", pass=" + pass + ", pnumber=" + pnumber + "]";
}

}
