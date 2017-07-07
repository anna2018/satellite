package com.entity;

import java.util.Date;

public class Satellite {
	private int satelliteId;
	private Date time;
	private int attribute;
	private double value;
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getAttribute() {
		return attribute;
	}
	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public int getSatelliteId() {
		return satelliteId;
	}
	public void setSatelliteId(int satelliteId) {
		this.satelliteId = satelliteId;
	}
	

}
