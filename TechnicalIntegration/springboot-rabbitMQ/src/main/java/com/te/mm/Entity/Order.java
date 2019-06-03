package com.te.mm.Entity;

import java.io.Serializable;

public class Order  implements Serializable{

	@Override
	public String toString() {
		return "Order [name=" + name + ", love=" + love + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLove() {
		return love;
	}
	public void setLove(String love) {
		this.love = love;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String love;
	
}
