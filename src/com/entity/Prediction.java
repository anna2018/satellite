package com.entity;

import java.util.Date;

public class Prediction {
	private int satelliteId;
	public int getSatelliteId() {
		return satelliteId;
	}
	public void setSatelliteId(int satelliteId) {
		this.satelliteId = satelliteId;
	}
	private String attribute;
	private Date time;
	private double predictedvalue;
	private double upper;
	private double lower;
	private double actualvalue;
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public double getPredictedvalue() {
		return predictedvalue;
	}
	public void setPredictedvalue(double predictedvalue) {
		this.predictedvalue = predictedvalue;
	}
	public double getUpper() {
		return upper;
	}
	public void setUpper(double upper) {
		this.upper = upper;
	}
	public double getLower() {
		return lower;
	}
	public void setLower(double lower) {
		this.lower = lower;
	}
	public double getActualvalue() {
		return actualvalue;
	}
	public void setActualvalue(double actualvalue) {
		this.actualvalue = actualvalue;
	}
	
}
