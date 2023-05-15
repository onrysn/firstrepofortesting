package com.mobit;

public class Item {

	public int id;
	public String desc;
	public Runnable r;

	public Item(int id, String desc, Runnable r) {
		this.id = id;
		this.desc = desc;
		this.r = r;
	}
}
